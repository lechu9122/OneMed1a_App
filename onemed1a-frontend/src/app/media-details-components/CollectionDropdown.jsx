'use client';

import { useEffect, useMemo, useState } from 'react';
import PropTypes from 'prop-types';
import {uppsertMediaStatus} from "@/api/mediaStatusApi";

export default function CollectionDropdown({
                                               userId,
                                               mediaId,
                                               mediaType,
                                               currentStatus,                 // enum ("COMPLETED") or display ("Completed") or undefined
                                               onMarkNotCompleted,            // async ({ userId, mediaId, mediaType }) => void
                                               onChange = () => {},
                                               disabled = false,
                                               className = '',
                                           }) {
    const labels = useMemo(() => ({
        COMPLETED: 'Completed',
        NOT_COMPLETED: 'Not Completed',
    }), []);

    const OPTIONS = useMemo(() => [labels.COMPLETED, labels.NOT_COMPLETED], [labels]);

    // Normalize incoming status to a display label
    const enumToDisplay = (val) => {
        if (!val) return labels.NOT_COMPLETED;
        const v = String(val).toUpperCase();
        if (v === 'COMPLETED') return labels.COMPLETED;
        // If caller already sent a display string, keep it if valid; otherwise default to Not Completed
        return OPTIONS.includes(val) ? val : labels.NOT_COMPLETED;
    };

    const [open, setOpen] = useState(false);
    const [status, setStatus] = useState(enumToDisplay(currentStatus));

    // Keep in sync if parent updates currentStatus
    useEffect(() => {
        setStatus(enumToDisplay(currentStatus));
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [currentStatus]);

    const handleSelect = async (option) => {
        if (disabled || !userId || !mediaId) return;

        const prev = status;
        setStatus(option);
        setOpen(false);

        try {
            if (option === labels.COMPLETED) {
                await uppsertMediaStatus({ userId, mediaId, status:"COMPLETED" });
            } else {
                await onMarkNotCompleted?.({ userId, mediaId, mediaType });
            }
            onChange(option);
        } catch (err) {
            console.error('Failed to update status:', err);
            setStatus(prev); // rollback on error
        }
    };

    return (
        <div className={`relative inline-block ${className}`}>
            <button
                onClick={() => !disabled && setOpen((p) => !p)}
                disabled={disabled}
                className="rounded-full bg-neutral-900 px-6 py-2.5 text-sm font-medium text-neutral-100 ring-1 ring-neutral-800 shadow-sm transition hover:bg-neutral-800 focus:outline-none focus-visible:ring-2 focus-visible:ring-neutral-500 disabled:opacity-60 disabled:cursor-not-allowed w-56 text-left"
            >
                {status} <span className="float-right ml-2">▼</span>
            </button>

            {open && (
                <ul className="absolute z-10 mt-1 w-full bg-white text-black rounded shadow-lg">
                    {OPTIONS.map((option) => (
                        <li key={option}>
                            <button
                                className={`block w-full text-left px-4 py-2 text-sm hover:bg-gray-200 ${option === status ? 'font-bold' : ''}`}
                                onClick={() => handleSelect(option)}
                            >
                                {option}
                            </button>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

CollectionDropdown.propTypes = {
    userId: PropTypes.string.isRequired,
    mediaId: PropTypes.string.isRequired,
    mediaType: PropTypes.oneOf(['MOVIE', 'TV', 'MUSIC', 'BOOKS']).isRequired,
    currentStatus: PropTypes.string,
    onMarkCompleted: PropTypes.func.isRequired,
    onMarkNotCompleted: PropTypes.func.isRequired,
    onChange: PropTypes.func,
    disabled: PropTypes.bool,
    className: PropTypes.string,
};
