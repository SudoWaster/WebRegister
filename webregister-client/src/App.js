import React, { Component } from 'react';
import Login from './components/Login';
import Header from './components/Header';

import './App.css';
import API from './API';

const APIUrl = "http://localhost:8080/WebRegisterAPI/";
const api = new API(APIUrl);

class App extends Component {
  constructor() {
    super();
    
    this.state = { };
    
    this.requestStateCheck = this.requestStateCheck.bind(this);
  }
  
  componentDidMount() {
    this.requestStateCheck(); 
  }
  
  requestStateCheck() {
    this.setState({ isAuthenticated: api.isAuthenticated() });
  }
  
  render() {
    if(!this.state.isAuthenticated && !api.isAuthenticated()) {
      return <Login api={api} onSubmit={this.requestStateCheck} />;
    }
    
    return (
      <div className="App">
        <Header api={api} onLogout={this.requestStateCheck} />
      
        <p className="App-intro">
          Hello world from WebRegister!
        </p>
      
      </div>
    );
  }
}

export default App;
