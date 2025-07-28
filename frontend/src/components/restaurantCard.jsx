import { useNavigate } from "react-router-dom";
import { BASE_URL } from "../../constants";

const RestaurantCard = ({ name, distance, priceRange, imageUrl, id }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/restaurant/${id}`);
  };

  return (
    <div
      onClick={handleClick}
      className="w-60 rounded-md border border-gray-200 shadow-sm hover:shadow-md transition duration-200 bg-white cursor-pointer"
    >
      <img
        src={imageUrl}
        alt={name}
        width="240"
        height="160"
        loading="lazy"
        decoding="async"
        className="h-40 w-full object-cover rounded-t-md"
      />
      <div className="p-4">
        <h3 className="text-base font-semibold text-gray-800">
          {name} - ({distance})
        </h3>
        <p className="text-sm text-gray-600 mt-1">{priceRange}</p>
      </div>
    </div>
  );
};

export default RestaurantCard;
