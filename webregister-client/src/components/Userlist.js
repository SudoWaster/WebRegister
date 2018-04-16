
import React, { Component } from 'react';
import User from './User';

import './css/User.css';

class Userlist extends Component {
  constructor() {
    super();
    
    this.state = { 
      users: [],
      user: false  
    };
    
    this.showUser = this.showUser.bind(this);
    this.showList = this.showList.bind(this);
  }
  
  componentDidMount() {
    this.props.api.get('user/all', false, true)
    .then((result) => {
      if(result.status === 200) {
        this.setState({ users: result.data });
      }
    });
  }
  
  showUser(id) {
    this.setState({ user: (<User key={'user-' + id} user={id} api={this.props.api} onLogout={this.props.onLogout} />)});
  }
  
  showList() {
    this.setState({ user: false });
  }
  
  render() {
    if(this.state.user) {
      return (
        <div>
          <button className="user-goback" onClick={() => { this.showList() }}>Wróć</button>
          
          {this.state.user}
        </div>)
    }
    
    let users = this.state.users.map((user) => (
        <div className="clickable row list-element" onClick={() => { this.showUser(user.id); }}>
          <div className="col-4 user-list-id">{user.id}</div> 
          <div className="col-4 user-list-mail">{user.mail}</div> 
          <div className="col-4 user-list-name">{user.firstname + ' ' + user.lastname}</div>
        </div>
      ));
    
    return (
      <div>
        <div className="user-list">
          <div className="row list-header">
            <div className="col-4">Id</div>
            <div className="col-4">Mail</div>
            <div className="col-4">Imię i nazwisko</div>
          </div>
          {users}
        </div>
      </div>
    );
  }
}

export default Userlist;