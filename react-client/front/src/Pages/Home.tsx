import React from "react";
import "./Home.css";

const Home = () => {
  return (
    <div className="container">
      <div className="left">
        <div className="top">
          <div className="img"></div>
          <p>PurseApp</p>
        </div>
        <div className="middle">
          <h1 className="text">Vos Finances reunies en un seul endroit</h1>
          <div className="image"></div>
          <p>
            Plongez dans les rapports, élaborez des budgets, synchronisez vos
            banques et profitez de la catégorisation automatique.
          </p>
          <div className="app">
            <div className="image2"></div>
            <h2>PURSEAPP</h2>
          </div>
        </div>
      </div>
      <div className="right"></div>
    </div>
  );
};
export default Home;
