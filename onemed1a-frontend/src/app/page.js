"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function LoginPage() {
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault(); // stop page reload
    setError("");

    try {
      // Try module API first (from `main`)
      let user;
      try {
        const mod = await import("@/api/userAPI");
        if (typeof mod?.checkAccountDetails === "function") {
          user = await mod.checkAccountDetails({ email, password });
        }
      } catch {
        // ignore and fall back to fetch below
      }

      // Fallback to HTTP fetch (from `frontend/user-ui`)
      if (!user) {
        const response = await fetch("http://localhost:8080/api/v1/accountcheck", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ email, password }),
        });

        if (!response.ok) {
          const err = await response.json().catch(() => ({}));
          throw new Error(err.message || "Login failed");
        }

        user = await response.json();
      }

      // Set cookie (from `main`) — 30 days
      if (user?.id) {
        document.cookie = `userId=${encodeURIComponent(user.id)}; Path=/; Max-Age=2592000; SameSite=Lax`;
      }

      // Redirect — prefer plural route but works even if only /movie exists
      router.push("/movies");
    } catch (err) {
      console.error(err);
      setError(err?.message || "Login failed");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8">
        <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
          Sign in to your account
        </h2>

        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          <div className="space-y-4">
            <input
              id="email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              className="appearance-none rounded-md block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
              placeholder="Email address"
            />
            <input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="appearance-none rounded-md block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
              placeholder="Password"
            />
          </div>

          {error && <p className="text-red-500 text-sm">{error}</p>}

          <button
            type="submit"
            className="w-full py-2 px-4 text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none"
          >
            Sign in
          </button>

          <div className="text-center text-sm text-gray-600">
            Don&apos;t have an account?{" "}
            <Link href="/signup" className="font-medium text-indigo-600 hover:text-indigo-500">
              Sign up
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
}
