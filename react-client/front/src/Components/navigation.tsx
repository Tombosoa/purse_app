import React from "react";
import "./navigation.css";

const Navigation = () => {
  return (
    <nav className="navigation">
      <div className="Logo"></div>
      <a href="/">Dashboard</a>
      <a href="/account">Account</a>
      <a href="">Transactions</a>
    </nav>
  );
};
export default Navigation;
