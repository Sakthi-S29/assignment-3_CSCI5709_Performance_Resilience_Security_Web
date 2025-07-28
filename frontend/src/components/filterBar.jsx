import React, { useState } from 'react';

const FilterSidebar = ({
  cuisines,
  setCuisines,
  types,
  setTypes,
  priceRange,
  setPriceRange
}) => {

  const toggleCheckbox = (group, setGroup, key) => {
    setGroup((prev) => ({ ...prev, [key]: !prev[key] }));
  };


  return (
    <div className="border rounded-xl p-8 w-full h-full shadow-lg space-y-6 bg-white max-w-xs">

    {/* Price Range */}
        <div>
            <h2 className="text-lg font-semibold mb-3">Price Range</h2>
            <div className="flex items-center justify-between text-sm mb-1">
                <span>$0</span>
                <span>${priceRange}</span>
            </div>
            <input
                type="range"
                min="0"
                max="100"
                value={priceRange}
                onChange={(e) => setPriceRange(parseInt(e.target.value))}
                className="w-full h-2 rounded-lg appearance-none cursor-pointer"
                style={{
                background: `linear-gradient(to right, #000000 0%, #000000 ${priceRange}%, #e5e7eb ${priceRange}%, #e5e7eb 100%)`,
                }}
            />
        </div>


      {/* Cuisine Checkboxes */}
      <div>
        <h2 className="text-lg font-semibold mb-3">Cuisine</h2>
        <div className="space-y-2">
          {Object.entries(cuisines).map(([key, value]) => (
            <label key={key} className="flex items-center gap-3 text-base">
              <input
                type="checkbox"
                checked={value}
                onChange={() => toggleCheckbox(cuisines, setCuisines, key)}
                className="w-5 h-5 accent-black"
              />
              {key}
            </label>
          ))}
        </div>
      </div>

      {/* Type Checkboxes */}
      <div>
        <h2 className="text-lg font-semibold mb-3">Type</h2>
        <div className="space-y-2">
          {Object.entries(types).map(([key, value]) => (
            <label key={key} className="flex items-center gap-3 text-base">
              <input
                type="checkbox"
                checked={value}
                onChange={() => toggleCheckbox(types, setTypes, key)}
                className="w-5 h-5 accent-black"
              />
              {key}
            </label>
          ))}
        </div>
      </div>
      <button
  onClick={() => {
    setCuisines({ Indian: false, Mexican: false, Lebanese: false });
    setTypes({ Cafe: false, 'Fine Dining': false, 'Food Truck': false });
    setPriceRange(100);
  }}
  className="w-full py-2 px-4 bg-gray-200 text-sm rounded hover:bg-gray-300 transition"
>
  Reset Filters
</button>

    </div>
  );
};

export default FilterSidebar;
