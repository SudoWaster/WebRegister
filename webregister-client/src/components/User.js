import React, { Component } from 'react';
import './css/User.css';

class User extends Component {
  
  constructor() {
    super();
    
    this.state = { user: {
      mail: '',
      firstname: '',
      lastname: '',
      type: 'UNAUTHORIZED',
      id: 0
    },
    groups: []};
  }
  
  componentDidMount() {
    let url = 'user';
    
    if(!(this.props.user === undefined || this.props.user === null)) {
      url += '/' + this.props.user;
    }
    
    this.props.api.get(url, false, true)
    .then((result) => {
      if(result.status === 200) {
        this.setState({ user: result.data });
      }
    })
    .then(() => {
      this.props.api.get('user/' + this.state.user.id + '/groups', false, true)
        .then((result) => {
          if(result.status === 200) {
            this.setState({ groups: result.data });
          }
        });
    });
    
  }
  
  render() {
    let groups = this.state.groups.map((group) => <li><span className="info-group-progress">{group.progress}%</span> <span className="info-group-name">{group.group.name}</span> </li>);
                                       
    if(this.state.groups.length === 0) {
      groups = <li>brak</li>; 
    }
                                       
    return (
      <div>
        <div className="row">
          <div className="col-6 col-sm-12">
            <div className="info-column"><p className="info-label">Id</p> {this.state.user.id}</div>
            <div className="info-column"><p className="info-label">Mail</p> {this.state.user.mail}</div>
            <div className="info-column"><p className="info-label">Imię</p> {this.state.user.firstname}</div>
            <div className="info-column"><p className="info-label">Nazwisko</p> {this.state.user.lastname}</div>
            <div className="info-column"><p className="info-label">Typ</p> {this.state.user.type}</div>
          </div>
          <div className="col-6 col-sm-6">Grupy: 
            <ul className="user-group-list">
              {groups}
            </ul>  
          </div>
        </div>
      </div>
    );
  }
}

export default User;