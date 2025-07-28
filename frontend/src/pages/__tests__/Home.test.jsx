import { render, screen } from '@testing-library/react'
import { Provider } from 'react-redux'
import { configureStore } from '@reduxjs/toolkit'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import Home from '../home'

vi.mock('../../components/filterBar', () => ({
  default: () => <div data-testid="filter-sidebar">Filter Sidebar</div>
}))

vi.mock('../../components/restaurantCard', () => ({
  default: ({ id, name, priceRange }) => (
    <div data-testid={`restaurant-card-${id}`}>
      <h3>{name}</h3>
      <p>{priceRange}</p>
    </div>
  )
}))

vi.mock('../../components/spinner', () => ({
  Spinner: () => <div data-testid="spinner">Loading...</div>
}))

vi.mock('../../store/effects/restaurantEffects', () => ({
  fetchRestaurants: () => ({ type: 'FETCH_RESTAURANTS' }),
  filterRestaurants: () => ({ type: 'FILTER_RESTAURANTS' })
}))

vi.mock('lodash', () => ({
  debounce: (fn) => {
    const debouncedFn = (...args) => fn(...args)
    debouncedFn.cancel = vi.fn()
    return debouncedFn
  }
}))

const createTestStore = (initialState = {}) => {
  return configureStore({
    reducer: {
      restaurants: (state = { restaurants: [], isLoading: false }, action) => state
    },
    preloadedState: {
      restaurants: {
        restaurants: [],
        isLoading: false,
        ...initialState
      }
    }
  })
}

const renderWithProvider = (component, initialState = {}) => {
  const store = createTestStore(initialState)
  return render(
    <Provider store={store}>
      {component}
    </Provider>
  )
}

describe('Home Component', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('renders filter sidebar', () => {
    renderWithProvider(<Home />)
    expect(screen.getByTestId('filter-sidebar')).toBeInTheDocument()
  })

  it('displays loading spinner when isLoading is true', () => {
    renderWithProvider(<Home />, { isLoading: true })
    expect(screen.getByTestId('spinner')).toBeInTheDocument()
  })

  it('displays restaurant cards when data is available', () => {
    const mockRestaurants = [
      { id: 1, name: 'Pizza Palace', priceRange: '$25 Per Person' },
      { id: 2, name: 'Burger House', priceRange: '$15 Per Person' }
    ]
    
    renderWithProvider(<Home />, { restaurants: mockRestaurants })
    
    expect(screen.getByTestId('restaurant-card-1')).toBeInTheDocument()
    expect(screen.getByTestId('restaurant-card-2')).toBeInTheDocument()
    expect(screen.getByText('Pizza Palace')).toBeInTheDocument()
    expect(screen.getByText('Burger House')).toBeInTheDocument()
  })

  it('has proper main layout structure', () => {
    renderWithProvider(<Home />)
    const main = screen.getByRole('main')
    expect(main).toHaveClass('relative')
  })

  it('shows no restaurant cards when restaurants array is empty', () => {
    renderWithProvider(<Home />, { restaurants: [] })
    expect(screen.queryByTestId(/restaurant-card-/)).not.toBeInTheDocument()
  })
}) 