import React, { useState } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { BASE_URL, getToken } from '../../constants';
import axios from 'axios';
import { useDispatch } from 'react-redux';
import { fetchRestaurantById } from '../store/effects/restaurantEffects';
import { toast } from 'react-toastify';

const AddReview = ({ isOpen, onClose, id }) => {
  const [rating, setRating] = useState(0);
  const [ratingTouched, setRatingTouched] = useState(false);
const dispatch = useDispatch()
  if (!isOpen) return null;

  const validationSchema = Yup.object({
    title: Yup.string(),
    content: Yup.string(), // optional
  });

  const addReview = (review) => {
        axios.post(BASE_URL + `/restaurants/${id}/reviews`, review, {
            headers: {
                Authorization: `${getToken()}`,
            },
        })
            .then(response => {
                dispatch(fetchRestaurantById(id));
            })
            .catch(err => {
                    toast.error(err?.response?.data?.message);
            });
    }

  const handleSubmit = (values) => {
    if (rating === 0) {
      setRatingTouched(true);
      return;
    }

    const reviewData = {
      ...values,
      rating,
    };

    addReview(reviewData)
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black/25 flex justify-center items-center z-50">
      <div className="bg-white p-6 rounded-lg shadow-lg w-[90%] max-w-md">
        <h2 className="text-xl font-bold mb-4 text-center">Add a Review</h2>
        <Formik
          initialValues={{ title: '', content: '' }}
          validationSchema={validationSchema}
          onSubmit={handleSubmit}
        >
          <Form className="space-y-4">
            <div>
              <Field
                name="title"
                type="text"
                placeholder="Review title"
                className="w-full border p-2 rounded"
              />
              <ErrorMessage
                name="title"
                component="div"
                className="text-red-500 text-sm mt-1"
              />
            </div>

            <Field
              as="textarea"
              name="content"
              placeholder="Write your review..."
              className="w-full border p-2 rounded h-24 resize-none"
            />

            <div>
              <div className="flex items-center gap-1">
                {[1, 2, 3, 4, 5].map((star) => (
                  <span
                    key={star}
                    onClick={() => {
                      setRating(star);
                      setRatingTouched(false);
                    }}
                    className={`text-2xl cursor-pointer ${
                      star <= rating ? 'text-yellow-400' : 'text-gray-400'
                    }`}
                  >
                    ★
                  </span>
                ))}
              </div>
              {ratingTouched && rating === 0 && (
                <div className="text-red-500 text-sm mt-1">Rating is required</div>
              )}
            </div>

            <div className="flex justify-end gap-3 pt-2">
              <button
                type="button"
                onClick={onClose}
                className="px-4 py-2 bg-gray-200 text-black rounded hover:bg-gray-300"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="px-4 py-2 bg-black text-white rounded hover:bg-gray-800"
              >
                Submit
              </button>
            </div>
          </Form>
        </Formik>
      </div>
    </div>
  );
};

export default AddReview;
