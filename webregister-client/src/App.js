import React, { Component } from 'react';
import Login from './components/Login';
import Header from './components/Header';
import Menu from './components/Menu';
import Landing from './components/Landing';

import './App.css';
import './grid.css';
import API from './API';

const APIUrl = "http://localhost:8080/WebRegisterAPI/";
const api = new API(APIUrl);

class App extends Component {
  constructor() {
    super();
    
    this.state = { menu: false, 
        content: (<Landing />) };
    
    this.requestStateCheck = this.requestStateCheck.bind(this);
    this.toggleMenu = this.toggleMenu.bind(this);
    this.toggleContent = this.toggleContent.bind(this);
  }
  
  componentDidMount() {
    this.requestStateCheck();
    api.setCallback(this.requestStateCheck);
  }
  
  requestStateCheck() {
    this.setState({ isAuthenticated: api.isAuthenticated() });
  }
  
  toggleMenu() {
    this.setState({ menu: !this.state.menu });
  }
  
  toggleContent(component) {
    this.setState({ content: component });
  }
  
  render() {
    if(!this.state.isAuthenticated && !api.isAuthenticated()) {
      return <Login api={api} onSubmit={this.requestStateCheck} />;
    }
    
    return (
      <div className="App">
        <Header api={api} onLogout={this.requestStateCheck} toggleMenu={this.toggleMenu} />
      
        <Menu api={api} display={this.state.menu} onSelect={this.toggleContent} onLogout={this.requestStateCheck} />
      
        <div className="container">
          {this.state.content}
        </div>
      </div>
    );
  }
}

export default App;
