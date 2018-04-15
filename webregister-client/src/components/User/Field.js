import React, { Component } from 'react';

class Field extends Component {
  constructor() {
    super();
    
    this.state = {};
    
    this.changeValue = this.changeValue.bind(this);
  }
  
  changeValue(e) {
    this.props.onEdit(e.target.value);
    
    e.preventDefault();
  }
  
  render() {
    let options = '';
    
    if(!(this.props.options === undefined)) {
      options = this.props.options.map((option) => <option value={option} selected={option === this.props.value}>{option}</option>);
    }

    let input = (<input type="text" value={this.props.value} disabled={!this.props.editable} onChange={this.changeValue} />);

    if(this.props.selectable !== undefined && this.props.selectable === true) {
      input = (<select defaultValue={this.props.value} onChange={this.changeValue}>{options}</select>)
    }
    
    return (
      <div className="info-field">
        <p className="info-label">{this.props.name}</p> {input}
      </div>
    );
  }
}

export default Field;