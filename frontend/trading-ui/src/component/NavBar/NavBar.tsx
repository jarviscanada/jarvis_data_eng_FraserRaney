//import React from 'react'
import './NavBar.scss'
import {NavLink} from 'react-router-dom'
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {
  faAddressBook as dashboardIcon,
  faHome as homeIcon,
  faMoneyBill as quoteIcon,
} from '@fortawesome/free-solid-svg-icons';

function NavBar() {
  return (
      <nav className="page-navigation">
        <NavLink
            to="/"
            className={({isActive}) =>
                `page-navigation-item ${isActive ? 'active' : ''}`
            }
            title="Dashboard"
        >
          <FontAwesomeIcon icon={homeIcon}/>
        </ NavLink>

        <NavLink
            to="/traders"
            className={({isActive}) =>
                `page-navigation-item ${isActive ? 'active' : ''}`
            }
            title="Traders"
        >
          <FontAwesomeIcon icon={dashboardIcon}/>
        </NavLink>

        <NavLink
            to="/quotes"
            className={({isActive}) =>
                `page-navigation-item ${isActive ? 'active' : ''}`
            }
            title="Quotes"
        >
          <FontAwesomeIcon icon={quoteIcon}/>
        </NavLink>
      </nav>
  )
}

export default NavBar