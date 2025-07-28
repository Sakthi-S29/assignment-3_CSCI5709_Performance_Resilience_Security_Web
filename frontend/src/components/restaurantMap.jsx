import React from "react";
import { GoogleMap, useJsApiLoader, Marker} from "@react-google-maps/api";

const containerStyle = {
  width: "100%",
  height: "400px"
};

const RestaurantMap = ({ latitude, longitude }) => {
  const { isLoaded, loadError } = useJsApiLoader({
    googleMapsApiKey: import.meta.env.VITE_MAPBOX_API_KEY // store key in .env
  });

  const center = {
    lat: latitude,
    lng: longitude
  };

  if (loadError) return <div>Error loading maps</div>;
  if (!isLoaded) return <div>Loading Maps...</div>;

  return (
    // <div className="d-flex justify-center items-center mx-20 mb-10">
    <div className="max-w-5xl mx-auto p-6">
        <GoogleMap
        mapContainerStyle={containerStyle}
        center={center}
        zoom={15}
        >
        <Marker position={center} />
        </GoogleMap>
    </div>
  );
};

export default RestaurantMap;
