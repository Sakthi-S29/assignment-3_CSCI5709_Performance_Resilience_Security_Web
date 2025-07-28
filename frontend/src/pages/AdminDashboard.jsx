import React, { useState, useEffect, lazy, Suspense } from 'react';
import axios from 'axios';
import { BASE_URL, getToken } from '../../constants';
import { toast } from 'react-toastify';
import Navbar from '../components/navbar';

// Lazy load heavy components
const RestaurantList = lazy(() => import('../components/admin/RestaurantList'));
const RestaurantEditor = lazy(() => import('../components/admin/RestaurantEditor'));
const CreateRestaurantModal = lazy(() => import('../components/admin/CreateRestaurantModal'));
const AdminNavbar = lazy(() => import('../components/admin/AdminNavbar'));

const AdminDashboard = () => {
  const [restaurants, setRestaurants] = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [selected, setSelected] = useState(null);
  const [createOpen, setCreateOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  const fetchRestaurants = async () => {
    try {
      const res = await axios.get(`${BASE_URL}/restaurants`, {
        headers: { Authorization: getToken() },
      });
      setRestaurants(res.data.data);
      setFiltered(res.data.data);
    } catch (err) {
      toast.error(err?.response?.data?.message);
    }
    setIsLoading(false);
  };

  useEffect(() => {
    fetchRestaurants();
  }, []);

  return (
    <div className="flex flex-col min-h-screen">
      <Suspense fallback={<div>Loading admin navbar...</div>}>
        <AdminNavbar />
      </Suspense>

      <Navbar isAdmin={true} />

      <div className="flex min-h-screen">
        <Suspense fallback={<div>Loading restaurant list...</div>}>
          <RestaurantList
            isLoading={isLoading}
            restaurants={filtered}
            setFiltered={setFiltered}
            all={restaurants}
            onSelect={(r) => setSelected({ ...r })}
            onCreate={() => setCreateOpen(true)}
          />
        </Suspense>

        <div className="flex-1 p-6 bg-gray-50">
          {selected ? (
            <Suspense fallback={<div>Loading editor...</div>}>
              <RestaurantEditor
                restaurant={selected}
                onUpdated={(shouldClear = false) => {
                  fetchRestaurants();
                  if (shouldClear) setSelected(null);
                }}
              />
            </Suspense>
          ) : (
            <p className="text-gray-500 text-center mt-20">
              Select a restaurant to edit.
            </p>
          )}
        </div>

        <Suspense fallback={<div>Loading create modal...</div>}>
          <CreateRestaurantModal
            isOpen={createOpen}
            onClose={() => setCreateOpen(false)}
            onCreated={fetchRestaurants}
          />
        </Suspense>
      </div>
    </div>
  );
};

export default AdminDashboard;
