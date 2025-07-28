import axios from 'axios';
import React, { useState } from 'react';
import { BASE_URL, getToken } from '../../constants';
import AddReview from './addReview';
import { toast } from 'react-toastify';
import {Spinner} from './spinner';

const Reservation = ({ restaurant }) => {
    const [tableFor, setTableFor] = useState('2');
    const [reservationTime, setReservationTime] = useState('');
    const [error, setError] = useState('');
const [isLoading, setIsLoading] = useState(false)

    const handleTimeChange = (e) => {
        const selected = new Date(e.target.value);
        const nowPlus30Min = new Date(Date.now() + 30 * 60 * 1000);
        const selectedHours = selected.getHours();
        if (selected < nowPlus30Min) {
            setError('Please select a time at least 30 minutes from now.');
        } else if (selectedHours < 10 || selectedHours >= 22) {
            setError('Booking time must be between 10:00 AM and 10:00 PM.');
        } else {
            setError('');
            setReservationTime(e.target.value);
        }
    };

    const minTime = new Date(Date.now() + 30 * 60 * 1000)
        .toISOString()
        .slice(0, 16);

    const reserve = () => {
        setIsLoading(true)
        axios.post(BASE_URL + '/bookings', {
            "restaurantId": restaurant?.id,
            "bookingDate": reservationTime,
            "bookingTime": reservationTime,
            "numberOfGuests": tableFor
        }, {
            headers: {
                Authorization: `${getToken()}`,
            },
        })
            .then(response => {
                      toast.success("Restaurant created successfully");
                setIsLoading(false)
                setReservationTime('')
                setTableFor(2)
            })
            .catch(err => {
                console.log(err)
                      toast.error(err?.response?.data?.error);
                setIsLoading(false)
            });
    }

    return (
        <div className="max-w-5xl mx-auto p-6 relative">
            <h2 className="text-2xl font-semibold mb-1">{restaurant?.name}</h2>
            <p className="text-gray-500 mb-3">{restaurant?.description}</p>

            <span className="inline-block bg-green-100 text-green-800 text-sm font-medium px-3 py-1 rounded-md mb-2">
                Reservation Charge
            </span>

            <div className="flex items-end space-x-2 mb-2">
                <span className="text-4xl font-bold text-gray-800">${restaurant?.reservationCharge}</span>
            </div>

            <p className="text-gray-500 mb-6">
                A reservation charge of ${restaurant?.reservationCharge} per person has to be paid to book table. This amount can be redeemed in the total bill amount.
            </p>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Table For*
                    </label>
                    <select
                        className="w-full border border-gray-300 rounded-md px-4 py-2 focus:outline-none focus:ring-2 focus:ring-black"
                        value={tableFor}
                        onChange={(e) => setTableFor(e.target.value)}
                    >
                        <option>2</option>
                        <option>3</option>
                        <option>4</option>
                        <option>5</option>
                        <option>6</option>
                        <option>7</option>
                        <option>8</option>
                    </select>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Time*
                    </label>
                    <input
                        type="datetime-local"
                        min={minTime}
                        className={`w-full border ${error ? 'border-red-500' : 'border-gray-300'
                            } rounded-md px-4 py-2 focus:outline-none focus:ring-2 ${error ? 'focus:ring-red-500' : 'focus:ring-black'
                            }`}
                        value={reservationTime}
                        onChange={handleTimeChange}
                    />
                    {error && <p className="text-red-500 text-sm mt-1">{error}</p>}
                </div>
            </div>
            {isLoading ? <Spinner /> :<button
                disabled={!reservationTime || error}
                className={`mt-6 px-6 py-2 rounded-md text-white font-medium transition w-full ${!reservationTime || error
                    ? 'bg-gray-400 cursor-not-allowed'
                    : 'bg-black hover:bg-gray-800 cursor-pointer'
                    }`}
                onClick={reserve}
            >
                Reserve
            </button>}
        </div>
    );
};

export default Reservation;
