import React, { Component } from 'react';

import Createbox from './Group/Createbox';
import Group from './Group';

import './css/Group.css';

class Grouplist extends Component {
  constructor() {
    super();
    
    this.state = { 
      userGroups: [],
      groups: [],
      priviledged: false,
      create: false
    };
    
    this.toggleCreate = this.toggleCreate.bind(this);
    this.openGroup = this.openGroup.bind(this);
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
  
  toggleCreate() {
    this.setState({ create: !this.state.create });
    this.componentDidMount();
  }
  
  openGroup(id) {
    this.props.onSelect(<Group key={'group-' + id} api={this.props.api} group={id} />);
  }
  
  render() {
    
    if(this.state.create) {
      return (
        <div>
          <button onClick={() => { this.toggleCreate(); }}>Wróć</button>
    
          <Createbox api={this.props.api} onSubmit={() => { this.toggleCreate(); }} />
        </div>
      );
    }
    
    
    let userGroupIds = [];
    
    let userGroups = this.state.userGroups.map((group) => {
      userGroupIds.push(group.id);
      
      return (
      <div key={'group-list-user-' + group.id} className="group-list-item user-group clickable row" onClick={() => { this.openGroup(group.id); }}>
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
        return '';
      }
      
      return (
      <div key={'group-list-' + group.id} className="group-list-item clickable row" onClick={() => { this.openGroup(group.id); }}>
        <div className="col-4 group-name">{group.name}</div>
        <div className="col-4 group-description">{group.description}</div>
        <div className="col-3 group-vacancies">{group.vacancies}</div>
        <div className="col-1 group-plus">+</div>
      </div>
      )
    });
    
    
    if(groups.length === counter) {
      groups = (<div className="row"><div className="col-12 info-column">Brak wolnych miejsc w innych grupach</div></div>);  
    }
  
    if(this.state.groups.length <= 0) {
      groups = (<div className="row"><div className="col-12 info-column">Brak grup</div></div>);
    }
    
    let addButton = '';
    
    if(this.state.priviledged) {
      addButton = (<button className="create-group-button" onClick={() => { this.toggleCreate(); }}>Stwórz grupę</button>);  
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