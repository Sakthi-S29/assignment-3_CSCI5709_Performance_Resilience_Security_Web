import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import AdminDashboard from '../AdminDashboard'
import axios from 'axios'

vi.mock('axios')

vi.mock('../../../constants', () => ({
  BASE_URL: 'http://localhost:3000',
  getToken: vi.fn(() => 'mock-token')
}))

vi.mock('react-toastify', () => ({
  toast: {
    error: vi.fn(),
    success: vi.fn()
  }
}))

vi.mock('../../components/admin/RestaurantList', () => ({
  default: ({ restaurants, onSelect, onCreate, isLoading }) => (
    <div data-testid="restaurant-list">
      {isLoading ? (
        <div data-testid="loading-restaurants">Loading restaurants...</div>
      ) : (
        <>
          <button onClick={onCreate} data-testid="create-restaurant-btn">
            Create Restaurant
          </button>
          {restaurants.map((restaurant) => (
            <div
              key={restaurant.id}
              onClick={() => onSelect(restaurant)}
              data-testid={`restaurant-item-${restaurant.id}`}
            >
              {restaurant.name}
            </div>
          ))}
        </>
      )}
    </div>
  )
}))

vi.mock('../../components/admin/RestaurantEditor', () => ({
  default: ({ restaurant, onUpdated }) => (
    <div data-testid="restaurant-editor">
      <h2>Editing: {restaurant.name}</h2>
      <button onClick={() => onUpdated(false)} data-testid="save-btn">
        Save
      </button>
      <button onClick={() => onUpdated(true)} data-testid="save-and-clear-btn">
        Save and Clear
      </button>
    </div>
  )
}))

vi.mock('../../components/admin/CreateRestaurantModal', () => ({
  default: ({ isOpen, onClose, onCreated }) => (
    isOpen ? (
      <div data-testid="create-modal">
        <button onClick={onClose} data-testid="close-modal-btn">
          Close
        </button>
        <button onClick={() => {
          onCreated()
          onClose()
        }} data-testid="create-restaurant-modal-btn">
          Create Restaurant
        </button>
      </div>
    ) : null
  )
}))

vi.mock('../../components/admin/AdminNavbar', () => ({
  default: () => <div data-testid="admin-navbar">Admin Navbar</div>
}))

vi.mock('../../components/navbar', () => ({
  default: ({ isAdmin }) => (
    <div data-testid="navbar">
      {isAdmin ? 'Admin Navbar' : 'Regular Navbar'}
    </div>
  )
}))

describe('AdminDashboard Component', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('renders admin dashboard with navbar', () => {
    render(<AdminDashboard />)
    
    expect(screen.getByTestId('navbar')).toBeInTheDocument()
    expect(screen.getByText('Admin Navbar')).toBeInTheDocument()
  })

  it('shows loading state initially', () => {
    render(<AdminDashboard />)
    
    expect(screen.getByTestId('loading-restaurants')).toBeInTheDocument()
  })

  it('fetches restaurants on mount', async () => {
    const mockRestaurants = [
      { id: 1, name: 'Restaurant 1' },
      { id: 2, name: 'Restaurant 2' }
    ]
    
    axios.get.mockResolvedValueOnce({
      data: { data: mockRestaurants }
    })
    
    render(<AdminDashboard />)
    
    await waitFor(() => {
      expect(axios.get).toHaveBeenCalledWith('http://localhost:3000/restaurants', {
        headers: { Authorization: 'mock-token' }
      })
    })
  })

  it('displays restaurants after successful fetch', async () => {
    const mockRestaurants = [
      { id: 1, name: 'Restaurant 1' },
      { id: 2, name: 'Restaurant 2' }
    ]
    
    axios.get.mockResolvedValueOnce({
      data: { data: mockRestaurants }
    })
    
    render(<AdminDashboard />)
    
    await waitFor(() => {
      expect(screen.getByTestId('restaurant-item-1')).toBeInTheDocument()
      expect(screen.getByTestId('restaurant-item-2')).toBeInTheDocument()
      expect(screen.getByText('Restaurant 1')).toBeInTheDocument()
      expect(screen.getByText('Restaurant 2')).toBeInTheDocument()
    })
  })

  it('shows create restaurant button', async () => {
    axios.get.mockResolvedValueOnce({
      data: { data: [] }
    })
    
    render(<AdminDashboard />)
    
    await waitFor(() => {
      expect(screen.getByTestId('create-restaurant-btn')).toBeInTheDocument()
    })
  })

  it('opens create modal when create button is clicked', async () => {
    axios.get.mockResolvedValueOnce({
      data: { data: [] }
    })
    
    render(<AdminDashboard />)
    
    await waitFor(() => {
      const createBtn = screen.getByTestId('create-restaurant-btn')
      fireEvent.click(createBtn)
    })
    
    expect(screen.getByTestId('create-modal')).toBeInTheDocument()
  })

  it('closes create modal when close button is clicked', async () => {
    axios.get.mockResolvedValueOnce({
      data: { data: [] }
    })
    
    render(<AdminDashboard />)
    
    await waitFor(() => {
      const createBtn = screen.getByTestId('create-restaurant-btn')
      fireEvent.click(createBtn)
    })
    
    const closeBtn = screen.getByTestId('close-modal-btn')
    fireEvent.click(closeBtn)
    
    expect(screen.queryByTestId('create-modal')).not.toBeInTheDocument()
  })

  it('selects restaurant when clicked', async () => {
    const mockRestaurants = [
      { id: 1, name: 'Restaurant 1' }
    ]
    
    axios.get.mockResolvedValueOnce({
      data: { data: mockRestaurants }
    })
    
    render(<AdminDashboard />)
    
    await waitFor(() => {
      const restaurantItem = screen.getByTestId('restaurant-item-1')
      fireEvent.click(restaurantItem)
    })
    
    expect(screen.getByTestId('restaurant-editor')).toBeInTheDocument()
    expect(screen.getByText('Editing: Restaurant 1')).toBeInTheDocument()
  })

  it('shows placeholder when no restaurant is selected', async () => {
    axios.get.mockResolvedValueOnce({
      data: { data: [] }
    })
    
    render(<AdminDashboard />)
    
    await waitFor(() => {
      expect(screen.getByText('Select a restaurant to edit.')).toBeInTheDocument()
    })
  })

  it('handles restaurant editor save without clearing', async () => {
    const mockRestaurants = [
      { id: 1, name: 'Restaurant 1' }
    ]
    
    axios.get.mockResolvedValue({
      data: { data: mockRestaurants }
    })
    
    render(<AdminDashboard />)
    
    await waitFor(() => {
      const restaurantItem = screen.getByTestId('restaurant-item-1')
      fireEvent.click(restaurantItem)
    })
    
    const saveBtn = screen.getByTestId('save-btn')
    fireEvent.click(saveBtn)
    
    await waitFor(() => {
      expect(axios.get).toHaveBeenCalledTimes(2)
    })
    
    expect(screen.getByTestId('restaurant-editor')).toBeInTheDocument()
  })


  it('handles API error when fetching restaurants', async () => {
    const mockError = {
      response: {
        data: {
          message: 'Failed to fetch restaurants'
        }
      }
    }
    
    axios.get.mockRejectedValueOnce(mockError)
    
    const { toast } = await import('react-toastify')
    
    render(<AdminDashboard />)
    
    await waitFor(() => {
      expect(toast.error).toHaveBeenCalledWith('Failed to fetch restaurants')
    })
  })

  it('refetches restaurants after creating new restaurant', async () => {
    axios.get.mockResolvedValue({
      data: { data: [] }
    })
    
    render(<AdminDashboard />)
    
    await waitFor(() => {
      const createBtn = screen.getByTestId('create-restaurant-btn')
      fireEvent.click(createBtn)
    })
    
    const createModalBtn = screen.getByTestId('create-restaurant-modal-btn')
    fireEvent.click(createModalBtn)
    
    await waitFor(() => {
      expect(axios.get).toHaveBeenCalledTimes(2)
    })
  })

}) 