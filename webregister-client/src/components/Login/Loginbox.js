import React, { Component } from 'react';

class Loginbox extends Component {
  
  constructor() {
    super();
    
    this.state = { error: false,
                  mail: '',
                  password: '',
                  processing: false
                 };
    
    this.changeMail = this.changeMail.bind(this);
    this.changePassword = this.changePassword.bind(this);
    this.authenticate = this.authenticate.bind(this);
  }
  
  authenticate(e) {
    this.setState({ processing: true })
    
    this.props.api.auth(this.state.mail, this.state.password)
      .then((result) => { 
      this.setState({ error: !result, processing: false }); 
      this.props.onSubmit();
    });
    
    e.preventDefault();
  }
  
  changeMail(e) {
    this.setState({ mail: e.target.value, error: false });
  }
  
  changePassword(e) {
    this.setState({ password: e.target.value, error: false });
  }
  
  render() {
    return (
      <form className={'login-form' + (this.state.error ? ' login-form-error' : '')} onSubmit={this.authenticate}>
            <input className="form-control" placeholder="Mail" value={this.state.mail} onChange={this.changeMail} type="text" disabled={this.state.processing} />
            <input className="form-control" placeholder="Hasło" value={this.state.password} onChange={this.changePassword} type="password" disabled={this.state.processing} />
            <input className="submit-button" type="submit" value="Zaloguj" disabled={this.state.processing} />
            <button onClick={() => {this.props.toggleRegister();}}>Zarejestruj</button>
      </form>
    );
  }
}

export default Loginbox;