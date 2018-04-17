import React, { Component } from 'react';

class Group extends Component {
  constructor() {
    super();
    
    this.state = {
      group: {
        name: '',
        description: '',
        vacancies: ''
      },
      instructors: [],
      priviledged: false,
      isin: false
    };
    
    this.join = this.join.bind(this);
    this.leave = this.leave.bind(this);
  }
  
  componentDidMount() {
    this.props.api.get('group/' + this.props.group, false, true)
    .then((result) => {
      if(result.status === 200) {
        this.setState({ group: result.data });
      }
    })
    .then(() => {
      this.props.api.get('group/' + this.props.group + '/instructors', false, true)
      .then((result) => {
        if(result.status === 200) {
          this.setState({ instructors: result.data });
        }
      })
      .then(() => {
        this.props.api.get('user', false, true)
        .then((result) => {
          if(result.status === 200) {
            const user = result.data;

            let instructorIds = [];

            for(let instructor of this.state.instructors) {
              instructorIds.push(instructor.id);
            }

            if(user.type === 'ADMIN' || instructorIds.includes(user.id)) {
              this.setState({ priviledged: true });
            }
          }
        });
      });  
    });
    
    this.props.api.get('user/groups', false, true)
      .then((result) => {
      
        if(result.status === 200) {
          const groups = result.data;
          
          let groupIds = [];
          
          for(let group of groups) {
            groupIds.push(group.id);
          }
          
          if(groupIds.includes(this.props.group)) {
            this.setState({ isin: true });
          }
        }
    });
  }
  
  join() {
    this.props.api.put('group/' + this.props.group + '/members', false, true)
    .then((result) => {
      if(result.status === 200) {
        this.componentDidMount();
      }
    })
  }
  
  leave() {
    this.props.api.delete('group/' + this.props.group + '/members', true)
    .then((result) => {
      if(result.status === 200) {
        this.componentDidMount();
      }
    });
  }
  
  render() {
    
    let instructors = this.state.instructors.map((instructor) => (<li key={'instructor-' + this.props.group + '-' + instructor.id}>{instructor.firstname + ' ' + instructor.lastname}</li>));
    
    if(this.state.instructors.length <= 0) {
      instructors = '- brak -';
    }
    
    let addButton = (!this.state.isin ? (<button onClick={() => { this.join(); }} disabled={this.state.group.vacancies <= 0}>Dołącz</button>) : (<button onClick={() => {this.leave(); }}>Opuść</button>));
    
    let manageButtons = '';
    
    if(this.state.priviledged) {
      manageButtons = (<span><button>Edytuj</button> <button>Usuń</button></span>);
    }
    
    return (
      <div>
        <div className="row">
          <div className="col-12"><h2 className="group-title">{this.state.group.name}</h2> {addButton} {manageButtons}</div>
        </div>
      
        <div className="row">
          <div className="col-12 group-description">{this.state.group.description}</div>
        </div>
      
        <div className="row group-instructors">
          <div className="col-12 instructors-title">Prowadzący</div>
          <div className="col-12"><ul>{instructors}</ul></div>
        </div>
      </div>
    );
  }
}

export default Group;