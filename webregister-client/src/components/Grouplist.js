import React, { Component } from 'react';

import './css/Group.css';

class Grouplist extends Component {
  constructor() {
    super();
    
    this.state = { 
      userGroups: [],
      groups: [],
      priviledged: false
    }
  }
  
  componentDidMount() {
    
    let user = { type: 'UNAUTHORIZED' };
    
    this.props.api.get('user', false, true)
    .then((result) => {
      if(result.status === 200) {
        user = result.data;
        
        if(user.type === 'PRIVILEDGED' || user.type === 'ADMIN') {
          this.setState({ priviledged: true });
        }
      }
    })
    .then(() => {
      
      let url = 'groups';
      
      if(user.type === 'ADMIN') {
        url += '/all';
      }
      
      this.props.api.get(url, false, true)
      .then((result) => {
        if(result.status === 200) {
          this.setState({ groups: result.data });
        }
      })
      .then(() => {
        this.props.api.get('user/groups', false, true)
        .then((result) => {
          if(result.status === 200) {
            this.setState({ userGroups: result.data });
          }
        })
      })
    })
  }
  
  render() {
    let userGroupIds = [];
    
    let userGroups = this.state.userGroups.map((group) => {
      userGroupIds.push(group.id);
      
      return (
      <div className="group-list-item user-group clickable row">
        <div className="col-4 group-name">{group.name}</div>
        <div className="col-4 group-description">{group.description}</div>
        <div className="col-4 group-vacancies">{group.vacancies}</div>
      </div>
      )
    });
    
    let counter = 0;
    
    let groups = this.state.groups.map((group) => {
      if(userGroupIds.includes(group.id)) {
        counter++;
        return;
      }
      
      return (
      <div className="group-list-item clickable row">
        <div className="col-4 group-name">{group.name}</div>
        <div className="col-4 group-description">{group.description}</div>
        <div className="col-3 group-vacancies">{group.vacancies}</div>
        <div className="col-1 group-plus">+</div>
      </div>
      )
    });
    
    
    if(groups.length === counter) {
      groups = (<div className="row"><div className="col-12 info-column">Brak wolnych grup</div></div>);  
    }
  
    if(this.state.groups.length <= 0) {
      groups = (<div className="row"><div className="col-12 info-column">Brak grup</div></div>);
    }
    
    let addButton = '';
    
    if(this.state.priviledged) {
      addButton = (<button className="create-group-button" onClick={() => {}}>Stwórz grupę</button>);  
    }
  
    return (
      <div className="open-group-list">
      {addButton}
        <div className="group-list-header row">
          <div className="col-4">Nazwa</div>
          <div className="col-4">Opis</div>
          <div className="col-4">Wolne miejsca</div>
        </div>
        {userGroups}
        <div className="group-list-separator"> </div>
        {groups}
      </div>
    );
  }
}

export default Grouplist;