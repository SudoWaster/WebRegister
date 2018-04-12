import React, { Component } from 'react';
import Landing from './Landing';
import User from './User';

import './css/Menu.css';

class Menu extends Component {
  constructor() {
    super();
    
    this.state = { 
      groups: [],
      userType: 'UNAUTHORIZED'
    };
  }
  
  componentDidMount() {
    this.props.api.get('user', false, true)
    .then((result) => {
      if(result.status === 200) {
        this.setState({ userType: result.data.type });
      }
    })
    
    this.props.api.get('user/groups', false, true)
    .then((result) => {
      if(result.status === 200) {
        this.setState({ groups: result.data });
      }
    })
  }
  
  render() {
    return (
      <div className={'menu' + (this.props.display ? ' menu-display' : '')}>
        <ul>
          <li><button onClick={() => { this.props.onSelect(<Landing />); }} >Strona główna</button></li>
          <li><button onClick={() => { this.props.onSelect(<User api={this.props.api} />); }}>Moje konto</button></li>
        </ul>
      </div>
    );
  }
}

export default Menu;