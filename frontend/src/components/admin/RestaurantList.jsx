import React, { useState } from 'react';
import { Spinner } from '../spinner';

const RestaurantList = ({ restaurants, setFiltered, all, onSelect, onCreate, isLoading }) => {
  const [search, setSearch] = useState('');

  const handleSearch = (e) => {
    const val = e.target.value;
    setSearch(val);
    setFiltered(
      all.filter(r =>
        r.name.toLowerCase().includes(val.toLowerCase()) ||
        r.cuisine.toLowerCase().includes(val.toLowerCase())
      )
    );
  };

  return (
    <div className="w-80 bg-white shadow p-4 border-r">
      <h2 className="text-lg font-semibold mb-4">Restaurants</h2>
      <input
        type="text"
        value={search}
        onChange={handleSearch}
        placeholder="Search..."
        className="w-full mb-4 px-3 py-2 border rounded"
      />
      <button
          className="bg-black w-full mb-4 hover:bg-gray-800 px-6 py-2 rounded-md text-white font-medium transition cursor-pointer"
          onClick={onCreate}
        >
          Add Restaurant
        </button>

      {isLoading ? <Spinner /> : <ul className="space-y-2 max-h-[70vh] overflow-auto">
        {restaurants.map((r) => (
          <li
            key={r.id}
            onClick={() => onSelect(r)}
            className="cursor-pointer p-2 border rounded hover:bg-gray-100"
          >
            <p className="font-medium">{r.name}</p>
            <p className="text-xs text-gray-500">{r.cuisine}</p>
          </li>
        ))}
      </ul>}
    </div>
  );
};

export default RestaurantList;
