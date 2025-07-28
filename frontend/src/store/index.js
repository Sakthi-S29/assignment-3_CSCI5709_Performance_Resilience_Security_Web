// src/store/index.js
import { configureStore } from '@reduxjs/toolkit';
import restaurantReducer from './reducers/restaurantReducers.js';

export const store = configureStore({
  reducer: {
    restaurants: restaurantReducer,
    restaurant: restaurantReducer
  },
});
