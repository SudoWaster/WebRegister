import React, { Component } from 'react';

import '../css/Group.css';

class Createbox extends Component {
  constructor() {
    super(); 
    
    this.state = {
      name: '',
      description: '',
      vacancies: '',
      error: false,
      processing: false
    };
    
    this.changeValue = this.changeValue.bind(this);
    this.submitForm = this.submitForm.bind(this);
  }
  
  changeValue(e) {
    this.setState({ error: false });
    
    let key = e.target.name;
    let value = e.target.value;
    
    this.setState({ [key]: value });
  }
  
  submitForm(e) {
    
    e.preventDefault();
    
    if(this.state.name.length <= 0
       || this.state.description.length <= 0
       || this.state.vacancies < 0) {
      this.setState({ error: true });
      return;
    }
    
    this.setState({ processing: true });
    
    this.props.api.post('groups', { name: this.state.name, description: this.state.description, vacancies: this.state.vacancies }, true)
    .then((result) => {
      if(result.status === 201) {
        this.props.onSubmit();
      }
      
      this.setState({ processing: false });
    });
  }
  
  render() {
    return (
      <div>
        <form onSubmit={this.submitForm} className={"create-group-form " + (this.state.error ? 'create-group-form-error' : '')}>
          <input className="form-field" disabled={this.state.processing} onChange={this.changeValue} name="name" type="text" placeholder="Nazwa grupy" />
          <input className="form-field" disabled={this.state.processing} onChange={this.changeValue} name="description" type="text" placeholder="Opis" />
          <input className="form-field" disabled={this.state.processing} onChange={this.changeValue} name="vacancies" type="number" defaultValue={0} />
          <input disabled={this.state.processing} onChange={this.changeValue} type="submit" value="Stwórz" />
        </form>
      </div>
    );
  }
}

export default Createbox;