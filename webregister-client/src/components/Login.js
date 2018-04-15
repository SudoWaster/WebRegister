import React, { Component } from 'react';
import Loginbox from './Login/Loginbox';
import Registerbox from './Login/Registerbox';

import './css/Login.css';

class Login extends Component {
  
  constructor() {
    super();
    
    this.state = { register: false };
    
    this.toggleRegister = this.toggleRegister.bind(this);
  }
  
  toggleRegister() {
    this.setState({ register: !this.state.register });
  }
  
  render() {
    
    let action = (<Loginbox api={this.props.api} onSubmit={this.props.onSubmit} toggleRegister={this.toggleRegister} />);
    
    if(this.state.register) {
      action = (<Registerbox api={this.props.api} onSubmit={this.props.onSubmit} toggleRegister={this.toggleRegister} />);
    }
    
    return (
      <div className="login-box">
        <div className="login-content">
          WebRegister

          {action}
        </div>
      </div>
    );
  }
}

export default Login;
