import React from "react";
import "./Formulaire.css";



const Formulaire = () => {
  return (
    <div className="container">
        <form action="#">
            <h1>Form</h1>
            <div className="details">
            <p>Yourname</p>
                <div className="input-box1">
                    <input type="text" placeholder="Firstname" required></input>
            
                    <input type="text" placeholder="Lastname" ></input>
                </div>
            </div>
            <div className="details">
               
            </div>
            <div className="details">
                <div className="input-box2">
                    <p>Birthdate</p>
                    <input type="date" placeholder="Birthdate" required></input>
                </div>
            </div>
            <div className="details">
                <div className="input-box2">
                    <p>Salary</p>
                    <input type="text" placeholder="Salary" required></input>
                </div>
            </div>
            <div className="details">
                <div className="input-box2">
                    <p>Account Id</p>
                    <input type="number" placeholder="Account" required></input>
                </div>
            </div>
            <div className="button">
                <input type="submit" value=" + Add Account" />
            </div>
        </form>

    </div>
  );
};
export default Formulaire;