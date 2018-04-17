import React, { Component } from 'react';
import Landing from './Landing';
import User from './User';
import Userlist from './Userlist';
import Grouplist from './Grouplist';
import Group from './Group';

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
    let groups = this.state.groups.map((group) => (<li key={'menu-group-list-' + group.id} className="menu-group-item"><span className="clickable" onClick={() => { this.props.onSelect(<Group api={this.props.api} group={group.id} key={'group-' + group.id} />) }}>{group.name}</span></li>));
    
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
          <li><button onSelect={this.props.onSelect} onClick={() => { this.props.onSelect(<Grouplist key="group-list" api={this.props.api} onSelect={this.props.onSelect} />); }}>Lista grup</button></li>
          {groups}
          <li className="separator"></li>
          
          <li><button onClick={() => { this.props.onSelect(<User key="user-self" api={this.props.api} onLogout={this.props.onLogout} />); }}>Moje konto</button> {userlist} <button onClick={this.handleLogout}>Wyloguj</button></li>
        </ul>
      </div>
    );
  }
}

export default Menu;