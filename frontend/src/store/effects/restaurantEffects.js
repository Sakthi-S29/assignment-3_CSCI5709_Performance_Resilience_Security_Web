// src/store/effects/restaurantEffects.js

import axios from 'axios';
import {
  setRestaurants,
  setLoading,
  setError,
  setRestaurant,
} from '../actions/restaurantActions';
import { BASE_URL, getToken } from '../../../constants';
import { toast } from 'react-toastify';

export const fetchRestaurants = () => async (dispatch) => {
  dispatch(setLoading(true));
  try {
    const response = await axios.get(`${BASE_URL}/restaurants`, {
      headers: {
        Authorization: `${getToken()}`,
      },
    });
    dispatch(setRestaurants(response.data));
    dispatch(setLoading(false));
  } catch (err) {
          toast.error(err?.response?.data?.message);
    dispatch(setError(err.response?.data?.message || err.message || 'Failed to fetch'));
    dispatch(setLoading(false));
  }
};

export const filterRestaurants = (filterData) => async (dispatch) => {
  dispatch(setLoading(true));
  try {
    const response = await axios.post(`${BASE_URL}/restaurants/filter`, filterData, {
      headers: {
        Authorization: `${getToken()}`,
      },
    });
    dispatch(setRestaurants(response.data));
    dispatch(setLoading(false));
  } catch (err) {
          toast.error(err?.response?.data?.message);
    
    dispatch(setError(err.response?.data?.message || err.message || 'Failed to filter'));
    dispatch(setLoading(false));
  }
};

export const fetchRestaurantById = (id, navigate) => async (dispatch) => {
  dispatch(setRestaurant({}));
  dispatch(setLoading(true));
  try {
    const response = await axios.get(`${BASE_URL}/restaurants/${id}`, {
      headers: {
        Authorization: `${getToken()}`,
      },
    });
    dispatch(setRestaurant(response.data));
    dispatch(setLoading(false))
  } catch (err) {
          toast.error(err?.response?.data?.message);
    
    dispatch(
      setError(err.response?.data?.message || err.message || 'Failed to fetch')
    );
    dispatch(setLoading(false))
    navigate("/404")
  }
};
