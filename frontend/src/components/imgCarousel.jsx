import React from 'react';
import Slider from 'react-slick';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';

// const images = [
//   'https://images.pexels.com/photos/376464/pexels-photo-376464.jpeg',
//   'https://images.pexels.com/photos/958545/pexels-photo-958545.jpeg',
//   'https://images.pexels.com/photos/262959/pexels-photo-262959.jpeg',
// ];

const NextArrow = ({ onClick }) => (
  <div
    className="absolute right-4 top-1/2 transform -translate-y-1/2 z-10 bg-white p-2 rounded-full shadow cursor-pointer hover:bg-gray-100"
    onClick={onClick}
  >
    <ChevronRight className="h-6 w-6 text-gray-800" />
  </div>
);

const PrevArrow = ({ onClick }) => (
  <div
    className="absolute left-4 top-1/2 transform -translate-y-1/2 z-10 bg-white p-2 rounded-full shadow cursor-pointer hover:bg-gray-100"
    onClick={onClick}
  >
    <ChevronLeft className="h-6 w-6 text-gray-800" />
  </div>
);

const ImageCarousel = ({images}) => {
  const settings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 3000,
    nextArrow: <NextArrow />,
    prevArrow: <PrevArrow />,
  };

  return (
     images && <div className="relative max-w-4xl mx-auto mt-10">
      {images.length > 1 ? <Slider {...settings}>
        {[...images]?.map((src, index) => (
          <div key={index}>
            <img
              src={src}
              alt={`Slide ${index}`}
              className="w-full h-100 object-cover rounded-2xl shadow-lg"
            />
          </div>
        ))} 
      </Slider>: <div>
            <img
              src={images?.[0]}
              alt={`Image`}
              className="w-full h-100 object-cover rounded-2xl shadow-lg"
            />
          </div>}
    </div>
  );
};

export default ImageCarousel;
