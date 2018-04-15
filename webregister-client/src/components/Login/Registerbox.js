import React, { Component } from 'react';

class Registerbox extends Component {
  
  constructor() {
    super();
    
    this.state = { 
      mail: '',
      firstname: '',
      lastname: '',
      password: '',
      verify: '',
      processing: false,
      wrongpass: false,
      wrongmail: false,
      error: false
    };
    
    this.setValue = this.setValue.bind(this);
    this.register = this.register.bind(this);
    this.authenticate = this.authenticate.bind(this);
  }
  
  setValue(e) {
    this.setState({ wrongpass: false, wrongmail: false, error: false });
    
    let key = e.target.name;
    let value = e.target.value;
    
    this.setState({ [key]: value });
    
    e.preventDefault();
  }
  
  register(e) {
    e.preventDefault();
    
    if(this.state.password !== this.state.verify
      || this.state.password.length < 8) {
      this.setState({ wrongpass: true });
      return;
    }
    
    this.setState({ processing: true });
    
    this.props.api.post('user', { mail: this.state.mail, password: this.state.password, firstname: this.state.firstname, lastname: this.state.lastname }, false)
    .then((result) => {
      
      if(result.status === 201) {
        this.authenticate();
      }
      
      if(result.status === 403) {
        this.setState({ wrongmail: true });
      } else if(result.status >= 400) {
        this.setState({ error: true });
      }
      
      this.setState({ processing: false });
    });
    
  }
  
  authenticate() {
    this.setState({ processing: true })
    
    this.props.api.auth(this.state.mail, this.state.password)
      .then((result) => { 
      this.setState({ error: !result, processing: false }); 
      this.props.onSubmit();
    });
  }
  
  render() {
    return (
      <form className={'login-form' + (this.state.error ? ' login-form-error' : '')} onSubmit={this.register}>
        
        <input className={"form-control" + (this.state.wrongmail ? ' login-error' : '')} name="mail" placeholder="Mail" value={this.state.mail} onChange={this.setValue} type="text" disabled={this.state.processing} />
        
        <input className="form-control" name="firstname" placeholder="Imię" value={this.state.firstname} onChange={this.setValue} type="text" disabled={this.state.processing} />
        <input className="form-control" name="lastname" placeholder="Nazwisko" value={this.state.lastname} onChange={this.setValue} type="text" disabled={this.state.processing} />
        
        <input className={"form-control" + (this.state.wrongpass ? ' login-error' : '')} name="password" placeholder="Hasło" value={this.state.password} onChange={this.setValue} type="text" disabled={this.state.processing} />
        <input className={"form-control" + (this.state.wrongpass ? ' login-error' : '')} name="verify" placeholder="Powtórz hasło" value={this.state.verify} onChange={this.setValue} type="text" disabled={this.state.processing} />
        
        <input className="submit-button" type="submit" value="Zarejestruj" disabled={this.state.processing} />
        <button onClick={() => {this.props.toggleRegister();}}>Powrót</button>
      </form>
    );
  }
}

export default Registerbox;