import React, { Component } from 'react';
import './css/Header.css';

class Header extends Component {
  constructor() {
    super();
    this.state = { user: undefined };
    this.handleLogout = this.handleLogout.bind(this);
  }
  
  componentDidMount() {
    this.props.api.get('user', false, true)
    .then((result) => {
      if(result.status === 200) {
        this.setState({ user: result.data });
      }
    });
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
    let name = '';
    
    if(this.state.user !== undefined) {
      name = this.state.user.firstname;
    }
    
    return (
      <div>
        <header className="App-header">
          <div className="left">
            WebRegister
          </div>
          <div className="right">
            Witaj, {name}! <a href="#" onClick={this.handleLogout}>Wyloguj</a>
          </div>
        </header>
        
        <div className="dummy-header"> </div>
      </div>
    );
  }
}

export default Header;