import React, { useState } from 'react';
import axios from 'axios';
import { BASE_URL, getToken } from '../../../constants';
import { toast } from 'react-toastify';

const CreateRestaurantModal = ({ isOpen, onClose, onCreated }) => {
  const [form, setForm] = useState({
    name: '', description: '', cuisine: '', contactNumber: '', address: '',
    priceRange: undefined, type: '', reservationCharge: undefined,
    keywords: [], imageUrls: [],
  });
  const [keywordsText, setKeywordsText] = useState('');
  const [imageUrlsText, setImageUrlsText] = useState('');

  if (!isOpen) return null;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async () => {
    const payload = {
      ...form,
      keywords: keywordsText.split(',').map(v => v.trim()).filter(Boolean),
      imageUrls: imageUrlsText.split(',').map(v => v.trim()).filter(Boolean),
    };
    try {
      await axios.post(`${BASE_URL}/admin/restaurant`, payload, {
        headers: { Authorization: getToken() },
      });
      toast.success("Restaurant created");
      setForm({
        name: '', description: '', cuisine: '', contactNumber: '', address: '',
        priceRange: undefined, type: '', reservationCharge: undefined,
        keywords: [], imageUrls: [],
      });
      setKeywordsText('');
      setImageUrlsText('');
      onCreated();
      onClose();
    } catch (err) {
      console.error("Create error", err);
      toast.error("Failed to create");
    }
  };

  return (
    <div className="fixed inset-0 bg-black/25 bg-opacity-40 flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded shadow-md w-[550px]">
        <h2 className="text-xl font-semibold mb-6">Add New Restaurant</h2>

        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-sm text-gray-700 mb-1">Name</label>
            <input name="name" value={form.name} onChange={handleChange} className="w-full border p-2 rounded" />
          </div>
          <div>
            <label className="block text-sm text-gray-700 mb-1">Description</label>
            <input name="description" value={form.description} onChange={handleChange} className="w-full border p-2 rounded" />
          </div>

          <div>
            <label className="block text-sm text-gray-700 mb-1">Cuisine</label>
            <input name="cuisine" value={form.cuisine} onChange={handleChange} className="w-full border p-2 rounded" />
          </div>
          <div>
            <label className="block text-sm text-gray-700 mb-1">Contact Number</label>
            <input name="contactNumber" value={form.contactNumber} onChange={handleChange} className="w-full border p-2 rounded" />
          </div>

          <div>
            <label className="block text-sm text-gray-700 mb-1">Address</label>
            <input name="address" value={form.address} onChange={handleChange} className="w-full border p-2 rounded" />
          </div>
          <div>
            <label className="block text-sm text-gray-700 mb-1">Price Range</label>
            <input name="priceRange" type="number" value={form.priceRange || ''} onChange={handleChange} className="w-full border p-2 rounded" />
          </div>

          <div>
            <label className="block text-sm text-gray-700 mb-1">Type</label>
            <input name="type" value={form.type} onChange={handleChange} className="w-full border p-2 rounded" />
          </div>
          <div>
            <label className="block text-sm text-gray-700 mb-1">Reservation Charge</label>
            <input name="reservationCharge" type="number" value={form.reservationCharge || ''} onChange={handleChange} className="w-full border p-2 rounded" />
          </div>
        </div>

        <div className="mt-4">
          <label className="block text-sm text-gray-700 mb-1">Keywords (comma separated)</label>
          <textarea
            name="keywords"
            value={keywordsText}
            onChange={(e) => setKeywordsText(e.target.value)}
            className="w-full border p-2 rounded"
          />
        </div>

        <div className="mt-4">
          <label className="block text-sm text-gray-700 mb-1">Image URLs (comma separated)</label>
          <textarea
            name="imageUrls"
            value={imageUrlsText}
            onChange={(e) => setImageUrlsText(e.target.value)}
            className="w-full border p-2 rounded"
          />
        </div>

        <div className="mt-6 flex justify-end gap-4">
          <button onClick={onClose} className="bg-gray-200 hover:bg-gray-300 px-4 py-2 rounded">Cancel</button>
          <button onClick={handleSubmit} className="bg-black text-white px-4 py-2 rounded hover:bg-gray-800">Create</button>
        </div>
      </div>
    </div>
  );
};

export default CreateRestaurantModal;
