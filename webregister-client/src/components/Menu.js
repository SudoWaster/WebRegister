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
    
    this.handleLogout = this.handleLogout.bind(this);
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
  
  handleLogout(e) {
    this.props.api.delete('auth', true)
    .then(() => {
      this.props.api.destroySession();
      this.props.onLogout();
    });
    
    e.preventDefault();
  }
  
  render() {
    const addGroup = (this.state.userType === 'PRIVILEDGED' || this.state.userType === 'ADMIN' ? (<button onClick={() => {}}>+</button>) : '');
    
    let groups = this.state.groups.map((group) => (<li><button>{group.name}</button></li>));
    
    if(this.state.groups.length === 0) {
      groups = (<li>- brak -</li>);
    }
    
    return (
      <div className={'menu' + (this.props.display ? ' menu-display' : '')}>
        <ul>
          <li><button onClick={() => { this.props.onSelect(<Landing key="landing-default" />); }} >Strona główna</button></li>
          
          <li className="separator"></li>
          <li>Twoje grupy {addGroup}</li>
          {groups}
          <li className="separator"></li>
          
          <li><button onClick={() => { this.props.onSelect(<User key="user-self" api={this.props.api} />); }}>Moje konto</button></li>
          <li><button onClick={this.handleLogout}>Wyloguj</button></li>
        </ul>
      </div>
    );
  }
}

export default Menu;