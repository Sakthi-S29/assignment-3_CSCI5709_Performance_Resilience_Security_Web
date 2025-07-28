import { render, screen, fireEvent } from '@testing-library/react'
import { Provider } from 'react-redux'
import { configureStore } from '@reduxjs/toolkit'
import { BrowserRouter } from 'react-router-dom'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import Restaurant from '../restaurant'

vi.mock('../../components/imgCarousel', () => ({
  default: ({ images }) => (
    <div data-testid="image-carousel">
      {images?.length ? `${images.length} images` : 'No images'}
    </div>
  )
}))

vi.mock('../../components/reservation', () => ({
  default: ({ restaurant }) => (
    <div data-testid="reservation">
      <h3>Reservation for {restaurant?.name}</h3>
    </div>
  )
}))

vi.mock('../../components/restaurantMap', () => ({
  default: ({ latitude, longitude }) => (
    <div data-testid="restaurant-map">
      Map at {latitude}, {longitude}
    </div>
  )
}))

vi.mock('../../components/restaurantReviews', () => ({
  default: ({ reviews }) => (
    <div data-testid="restaurant-reviews">
      {reviews?.length ? `${reviews.length} reviews` : 'No reviews'}
    </div>
  )
}))

vi.mock('../../components/addReview', () => ({
  default: ({ isOpen, onClose, id }) => (
    <div data-testid="add-review" style={{ display: isOpen ? 'block' : 'none' }}>
      Add Review for restaurant {id}
      <button onClick={onClose}>Close</button>
    </div>
  )
}))

vi.mock('../../components/modal', () => ({
  default: ({ isOpen, onClose }) => (
    <div data-testid="modal" style={{ display: isOpen ? 'block' : 'none' }}>
      Modal Content
      <button onClick={onClose}>Close Modal</button>
    </div>
  )
}))

vi.mock('../../components/spinner', () => ({
  Spinner: () => <div data-testid="spinner">Loading...</div>
}))

const mockNavigate = vi.fn()
const mockParams = { id: '123' }

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,
    useNavigate: () => mockNavigate,
    useParams: () => mockParams
  }
})

vi.mock('axios', () => ({
  default: {
    get: vi.fn().mockResolvedValue({ data: { data: [] } })
  }
}))

vi.mock('../../store/effects/restaurantEffects', () => ({
  fetchRestaurantById: () => ({ type: 'FETCH_RESTAURANT_BY_ID' })
}))

vi.mock('../../../constants', () => ({
  BASE_URL: 'http://localhost:3000/api',
  getToken: () => 'mock-token'
}))

vi.mock('react-toastify', () => ({
  toast: {
    error: vi.fn()
  }
}))

const createTestStore = (initialState = {}) => {
  return configureStore({
    reducer: {
      restaurant: (state = { restaurant: null, isLoading: false }, action) => state
    },
    preloadedState: {
      restaurant: {
        restaurant: null,
        isLoading: false,
        ...initialState
      }
    }
  })
}

const renderWithProviders = (component, initialState = {}) => {
  const store = createTestStore(initialState)
  return render(
    <Provider store={store}>
      <BrowserRouter>
        {component}
      </BrowserRouter>
    </Provider>
  )
}

describe('Restaurant Component', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('displays loading spinner when isLoading is true', () => {
    renderWithProviders(<Restaurant />, { isLoading: true })
    
    expect(screen.getByTestId('spinner')).toBeInTheDocument()
    expect(screen.getByText('Loading...')).toBeInTheDocument()
  })

  it('renders all restaurant components when data is available', () => {
    const mockRestaurant = {
      id: '123',
      name: 'Pizza Palace',
      imageUrls: ['image1.jpg', 'image2.jpg'],
      overallReview: { href: '/reviews/123' }
    }

    renderWithProviders(<Restaurant />, { 
      restaurant: mockRestaurant,
      isLoading: false 
    })
    
    expect(screen.getByTestId('image-carousel')).toBeInTheDocument()
    expect(screen.getByTestId('reservation')).toBeInTheDocument()
    expect(screen.getByTestId('restaurant-map')).toBeInTheDocument()
    expect(screen.getByTestId('restaurant-reviews')).toBeInTheDocument()
    expect(screen.getByText('Reservation for Pizza Palace')).toBeInTheDocument()
  })

  it('opens add review modal when Add Review button is clicked', () => {
    const mockRestaurant = {
      id: '123',
      name: 'Pizza Palace',
      imageUrls: ['image1.jpg']
    }

    renderWithProviders(<Restaurant />, { 
      restaurant: mockRestaurant,
      isLoading: false 
    })
    
    const addReviewButton = screen.getByText('Add Review')
    fireEvent.click(addReviewButton)
    
    expect(screen.getByTestId('modal')).toBeInTheDocument()
    expect(screen.getByText('Modal Content')).toBeInTheDocument()
  })

  it('displays correct number of images in carousel', () => {
    const mockRestaurant = {
      id: '123',
      name: 'Pizza Palace',
      imageUrls: ['image1.jpg', 'image2.jpg', 'image3.jpg']
    }

    renderWithProviders(<Restaurant />, { 
      restaurant: mockRestaurant,
      isLoading: false 
    })
    
    expect(screen.getByText('3 images')).toBeInTheDocument()
  })

  it('displays restaurant map with correct coordinates', () => {
    const mockRestaurant = {
      id: '123',
      name: 'Pizza Palace',
      imageUrls: ['image1.jpg']
    }

    renderWithProviders(<Restaurant />, { 
      restaurant: mockRestaurant,
      isLoading: false 
    })
    
    expect(screen.getByText('Map at 44.638452391512345, -63.590358497425484')).toBeInTheDocument()
  })
}) 