import React, { Component } from 'react';
import Landing from './Landing';
import User from './User';
import Userlist from './Userlist';

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
    
    let groups = this.state.groups.map((group) => (<button>{group.name}</button>));
    
    if(this.state.groups.length === 0) {
      groups = (<li>- brak -</li>);
    }
                      
    let userlist = '';
    
    if(this.state.userType === 'ADMIN') {
      userlist = (<button onClick={() => { this.props.onSelect(<Userlist key="user-list" api={this.props.api} />); }}>Lista użytkowników</button>);
    }
    
    return (
      <div className={'menu' + (this.props.display ? ' menu-display' : '')}>
        <ul>
          <li><button onClick={() => { this.props.onSelect(<Landing key="landing-default" />); }} >Strona główna</button></li>
          
          <li className="separator"></li>
          <li>Twoje grupy {addGroup}</li>
          <li>{groups}</li>
          <li className="separator"></li>
          
          <li><button onClick={() => { this.props.onSelect(<User key="user-self" api={this.props.api} onLogout={this.props.onLogout} />); }}>Moje konto</button> {userlist} <button onClick={this.handleLogout}>Wyloguj</button></li>
        </ul>
      </div>
    );
  }
}

export default Menu;