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
          <h1 className="text">Your Finances in one place</h1>
          <div className="image"></div>
          <p>
          Dive into reports, build budgets, sync your
            banks and benefit from automatic categorization.
          </p>
          <div className="app">
            <div className="image2"></div>
            <h2>PURSEAPP</h2>
          </div>
        </div>
      </div>
      <div className="right">
        <h3>All Accounts</h3>
        <table>
  <tr>
    <th>Name</th>
    <th>LastName</th>
    <th>Birthdate</th>
    <th>Salary</th>
    <th>Count Id</th>
  </tr>
  <tr>
    <td>Rakoto</td>
    <td> Anders</td>
    <td>12 Mai 1992</td>
    <td>100000$</td>
    <td>123456789</td>
    <button>delete</button>
    <button>modify</button>
  </tr>
</table>
<button>Add a new Account</button>
      </div>
    </div>
  );
};
export default Home;
