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
            <div className="menu-icon" onClick={this.props.toggleMenu}>
              <div></div>
              <div></div>
              <div></div>
            </div>
            
            <span className="title">
              WebRegister
            </span>
          </div>
          <div className="right">
            Witaj, {name}! <span className="clickable" onClick={this.handleLogout}>Wyloguj</span>
          </div>
        </header>
        
        <div className="dummy-header"> </div>
      </div>
    );
  }
}

export default Header;