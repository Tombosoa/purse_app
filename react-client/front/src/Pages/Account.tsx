import React from "react";
import "./Account.css";
import Navigation from "../Components/navigation.tsx";
import { GrFormSearch } from "react-icons/gr";
import { IoAdd } from "react-icons/io5";

const Account = () => {
  return (
    <div className="container">
      <Navigation />
      <div className="main">
        <div className="account">
          <h3>Account</h3>
          <button><IoAdd />Add</button>
          <input type="search" placeholder="Search" />
          <p>Sort by</p>
          <select name="select">
          <option value="value1">Default</option>
          </select>
        </div>
        <div className="species">
            <div className="symbol">
                <div className="argent"></div>
                <div>Species</div>
            </div>
            <div className="spc">Species</div>
            <div className="value">0 MGA</div>
        </div>
      </div>
    </div>
  );
};
export default Account;
