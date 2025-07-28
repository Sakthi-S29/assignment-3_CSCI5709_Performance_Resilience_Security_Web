import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { BASE_URL, getToken } from '../../../constants';
import { toast } from 'react-toastify';

const RestaurantEditor = ({ restaurant, onUpdated }) => {
  const [form, setForm] = useState({ ...restaurant });
  const [keywordsText, setKeywordsText] = useState('');
  const [imageUrlsText, setImageUrlsText] = useState('');

  useEffect(() => {
    setForm({ ...restaurant });
    setKeywordsText((restaurant.keywords || []).join(', '));
    setImageUrlsText((restaurant.imageUrls || []).join(', '));
  }, [restaurant]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleUpdate = async () => {
    try {
      const payload = {
        ...form,
        keywords: keywordsText.split(',').map(v => v.trim()).filter(Boolean),
        imageUrls: imageUrlsText.split(',').map(v => v.trim()).filter(Boolean),
      };
      await axios.put(`${BASE_URL}/admin/restaurant/${restaurant.id}`, payload, {
        headers: { Authorization: getToken() },
      });
      toast.success("Updated successfully");
      onUpdated();
    } catch (err) {
      console.error("Update error", err);
      toast.error("Failed to update");
    }
  };

  const handleDelete = async () => {
    if (!window.confirm("Are you sure to delete this restaurant?")) return;
    try {
      await axios.delete(`${BASE_URL}/admin/restaurant/${restaurant.id}`, {
        headers: { Authorization: getToken() },
      });
      toast.success("Deleted successfully");
      onUpdated(true);
    } catch (err) {
      console.error("Delete error", err);
      toast.error("Failed to delete");
    }
  };

  return (
    <div className="bg-white p-6 rounded shadow">
      <h2 className="text-xl font-semibold mb-4">Edit Restaurant</h2>
      <div className="grid grid-cols-2 gap-4">
        {['name', 'description', 'cuisine', 'contactNumber', 'address', 'priceRange', 'type', 'reservationCharge'].map(field => (
          <input
            key={field}
            name={field}
            value={form[field] || ''}
            onChange={handleChange}
            placeholder={field}
            className="border p-2 rounded"
          />
        ))}
        <textarea
          name="keywords"
          value={keywordsText}
          onChange={(e) => setKeywordsText(e.target.value)}
          placeholder="Keywords (comma separated)"
          className="col-span-2 border p-2 rounded"
        />
        <textarea
          name="imageUrls"
          value={imageUrlsText}
          onChange={(e) => setImageUrlsText(e.target.value)}
          placeholder="Image URLs (comma separated)"
          className="col-span-2 border p-2 rounded"
        />
      </div>
      <div className="mt-4 flex gap-4">
        <button onClick={handleUpdate} className="bg-black text-white px-4 py-2 rounded hover:bg-gray-800">
          Save Changes
        </button>
        <button onClick={handleDelete} className="bg-gray-300 px-4 py-2 rounded hover:bg-gray-400">
          Delete
        </button>
      </div>
    </div>
  );
};

export default RestaurantEditor;
