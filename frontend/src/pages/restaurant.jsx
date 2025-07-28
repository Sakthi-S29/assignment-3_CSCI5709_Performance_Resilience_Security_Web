import React, { useEffect, useState } from 'react'
import ImageCarousel from '../components/imgCarousel'
import Reservation from '../components/reservation'
import RestaurantMap from '../components/restaurantMap'
import RestaurantReviews from '../components/restaurantReviews'
import axios from 'axios'
import { BASE_URL, getToken } from '../../constants'
import { useDispatch, useSelector } from 'react-redux'
import { fetchRestaurantById } from '../store/effects/restaurantEffects'
import { useNavigate, useParams } from 'react-router-dom'
import AddReview from '../components/addReview'
import { Spinner } from '../components/spinner'
import Modal from '../components/modal'
import { toast } from 'react-toastify'


const Restaurant = () => {

  const { id } = useParams();
  const dispatch = useDispatch()
  const [reviews, setReviews] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const navigate = useNavigate();


  const { restaurant, isLoading } = useSelector((state) => { return state.restaurant });

  useEffect(() => {
    dispatch(fetchRestaurantById(id, navigate));
  }, [dispatch, id]);



  useEffect(() => {
    const fetchReviewsByUrl = async () => {
      if (restaurant?.overallReview?.href) {
        try {
          const url = `${BASE_URL.slice(0, -4)}${restaurant.overallReview.href}`;
          const response = await axios.get(url, {
            headers: {
              Authorization: `${getToken()}`,
            },
          });
          // Ensure reviews is always an array
          setReviews(Array.isArray(response.data.data) ? response.data.data : []);
        } catch (err) {
                toast.error(err?.response?.data?.message);
          
          setReviews([]); // fallback to empty array on error
        }
      } else {
        setReviews([]); // fallback if no href
      }
    };
    fetchReviewsByUrl();
  }, [restaurant]);
  return (
     (isLoading ?<div className="flex items-center justify-center h-[100vh] w-full">
    <Spinner />
  </div> : <div className="flex-1 justify-center">
      <Modal isOpen={showModal} onClose={() => setShowModal(false)} />
      <div className="overflow-y-auto max-h-[calc(100vh-4rem)] pr-2">
        <ImageCarousel images={restaurant?.imageUrls} />
        <Reservation restaurant={restaurant} />
        <RestaurantMap latitude={44.638452391512345} longitude={-63.590358497425484} />
                     <div className="flex justify-center">
        <button
          className="bg-black hover:bg-gray-800 px-6 py-2 rounded-md text-white font-medium transition cursor-pointer"
          onClick={() => setShowModal(true)}
        >
          Add Review
        </button>
      </div>
        <RestaurantReviews reviews={reviews} />
        <AddReview
          isOpen={showModal}
          onClose={() => setShowModal(false)}
          id={restaurant?.id}
        />
        
      </div>
    </div>)
  )
}

export default Restaurant