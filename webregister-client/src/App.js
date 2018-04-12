import React, { Component } from 'react';
import Login from './components/Login';
import Header from './components/Header';
import Menu from './components/Menu';

import './App.css';
import API from './API';

const APIUrl = "http://localhost:8080/WebRegisterAPI/";
const api = new API(APIUrl);

class App extends Component {
  constructor() {
    super();
    
    this.state = { menu: false, 
        screen: (
        <p className="App-intro">
          Hello world from WebRegister!
        </p>
        ) };
    
    this.requestStateCheck = this.requestStateCheck.bind(this);
    this.toggleMenu = this.toggleMenu.bind(this);
    this.toggleScreen = this.toggleScreen.bind(this);
  }
  
  componentDidMount() {
    this.requestStateCheck(); 
  }
  
  requestStateCheck() {
    this.setState({ isAuthenticated: api.isAuthenticated() });
  }
  
  toggleMenu() {
    this.setState({ menu: !this.state.menu });
  }
  
  toggleScreen(component) {
    this.setState({ screen: component });
  }
  
  render() {
    if(!this.state.isAuthenticated && !api.isAuthenticated()) {
      return <Login api={api} onSubmit={this.requestStateCheck} />;
    }
    
    return (
      <div className="App">
        <Header api={api} onLogout={this.requestStateCheck} toggleMenu={this.toggleMenu} />
      
        <Menu api={api} display={this.state.menu} onSelect={this.toggleScreen} />
      
        {this.state.screen}
      
      </div>
    );
  }
}

export default App;
