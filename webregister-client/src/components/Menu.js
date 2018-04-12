import React, { Component } from 'react';
import './css/Menu.css';

class Menu extends Component {
  constructor() {
    super();
  }
  
  render() {
    return (
      <div className={'menu' + (this.props.display ? ' menu-display' : '')}>
        <ul>
          <li>Strona główna</li>
        </ul>
      </div>
    );
  }
}

export default Menu;