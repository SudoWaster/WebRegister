import React, { Component } from 'react';

class User extends Component {
  
  constructor() {
    super();
    
    this.state = { user: {
      mail: '',
      firstname: '',
      lastname: '',
      type: 'UNAUTHORIZED',
      id: 0
    } };
  }
  
  componentDidMount() {
    let url = 'user';
    
    if(!(this.props.user === undefined || this.props.user === null)) {
      url += '/' + this.props.user;
    }
    
    this.props.api.get(url, false, true)
    .then((result) => {
      if(result.status === 200) {
        this.setState({ user: result.data });
      }
    });
  }
  
  render() {
    return (
      <div>
        <ul>Id: {this.state.user.id}</ul>
        <ul>Mail: {this.state.user.mail}</ul>
        <ul>Imię: {this.state.user.firstname}</ul>
        <ul>Nazwisko: {this.state.user.lastname}</ul>
        <ul>Typ: {this.state.user.type}</ul>
      </div>
    );
  }
}

export default User;