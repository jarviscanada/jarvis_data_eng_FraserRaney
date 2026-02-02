//import React from 'react'
import './Dashboard.scss'
import NavBar from "../../component/NavBar/NavBar.tsx";

function Dashboard(props: unknown) {
  console.log(props);
  return (
      <div className="dashboard">
        {/* Include NavBar below */}
        <NavBar/>
        <div className="dashboard-content">Dashboard Content</div>
      </div>
  )
}

export default Dashboard