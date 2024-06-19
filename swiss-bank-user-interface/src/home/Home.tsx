import React, { useState, useEffect } from "react";
import "./Home.css";

const Home = () => {
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const images = [
    "home-screen/images/image2-min.jpg",
    "home-screen/images/image3-min.jpg",
    "home-screen/images/image4-min.jpg",
    "home-screen/images/image5-min.jpg",
    "home-screen/images/image6-min.jpg",
    "home-screen/images/image7-min.jpg",
  ];

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentImageIndex((prevIndex) => (prevIndex === images.length - 1 ? 0 : prevIndex + 1));
    }, 5000);

    return () => clearInterval(interval);
  }, [images.length]);

  return (
    <div className="home-screen">
      <div className="title">Welcome to Swiss Bank</div>
      <div className="bio">
        <p>We are committed to providing our clients with the highest level of service and security.</p>
      </div>
      <div className="image-container">
        {images.map((image, index) => (
          <img
            key={index}
            src={image}
            loading="lazy"
            alt="Swiss Bank Home Screen Image"
            className={`bank-image ${index === currentImageIndex ? "active" : ""}`}
          />
        ))}
      </div>
    </div>
  );
};

export default Home;
