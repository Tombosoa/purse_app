import React from "react";
import logo from "./logo.svg";
import "./App.css";
import Dashboard from "./Pages/Dashboard";
import Account from "./Pages/Account";
import { Routes, Route } from "react-router-dom";

function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/account" element={<Account />} />
      </Routes>
    </>
  );
}

export default App;
