
import {
    SET_RESTAURANTS,
    SET_LOADING,
    SET_ERROR,
    SET_RESTAURANT,
} from '../actions/restaurantActions';

const initialState = {
    restaurants: [],
    restaurant: {},
    isLoading: true,
    error: null,
};

const restaurantReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_LOADING:
            return { ...state, isLoading: action.payload };

        case SET_RESTAURANTS:
            return { ...state, restaurants: action.payload.data, isLoading: false, error: null };

        case SET_RESTAURANT:
            return { ...state, restaurant: action.payload.data, isLoading: false };

        case SET_ERROR:
            return { ...state, error: action.payload, isLoading: false };

        default:
            return state;
    }
};

export default restaurantReducer;
