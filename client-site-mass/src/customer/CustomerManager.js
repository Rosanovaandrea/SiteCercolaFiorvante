import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { BrowserRouter as Router, Routes, Route, Link, useNavigate, useParams } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css'; // Importa Bootstrap CSS

const API_BASE_URL = 'http://localhost:8080/api/customer'; // Assicurati che corrisponda al tuo backend Spring

// Componente per l'inserimento dei clienti
const InserisciCliente = () => {
  const [newCustomer, setNewCustomer] = useState({
    surname: '',
    name: '',
    email: '',
    phoneNumber: ''
  });
  const [message, setMessage] = useState('');
  const [error, setError] = useState({}); // Usa un oggetto per gestire gli errori di campo
  const [isSubmitting, setIsSubmitting] = useState(false);

    const validateEmail = (email) => {
        const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(String(email).toLowerCase());
    };

    const validatePhoneNumber = (phoneNumber) => {
        return /^[0-9]{10}$/.test(phoneNumber);
    };

  const handleInputChange = (e, key) => {
    setNewCustomer(prevState => {
      const newState = {
        ...prevState,
        [key]: e.target.value
      };
        let newError = {};
        if (key === 'email' && newState.email && !validateEmail(newState.email)) {
            newError = { ...error, email: 'Email non valida' };
        } else if (key === 'email') {
            newError = { ...error, email: undefined }; //Rimuovi l'errore
        }
        if (key === 'phoneNumber' && newState.phoneNumber && !validatePhoneNumber(newState.phoneNumber)) {
            newError = { ...error, phoneNumber: 'Numero di telefono non valido (10 cifre)' };
        } else if (key === 'phoneNumber') {
             newError = { ...error, phoneNumber: undefined };
        }
      setError(newError);
      return newState;
    });
  };

    const isFormValid = () => {
        return newCustomer.name &&
            newCustomer.surname &&
            newCustomer.email &&
            validateEmail(newCustomer.email) &&
            newCustomer.phoneNumber &&
            validatePhoneNumber(newCustomer.phoneNumber);
    };

  const handleSubmit = async (e) => {
    e.preventDefault();

      if (!isFormValid()) {
          setError({
            ...(!newCustomer.name ? { name: "Nome richiesto" } : {}),
            ...(!newCustomer.surname ? { surname: "Cognome richiesto" } : {}),
            ...(!newCustomer.email ? { email: "Email Richiesta" } : {}),
            ...(!newCustomer.phoneNumber ? { phoneNumber: "Numero di telefono richiesto" } : {})
          });
          setMessage('');
          return;
      }
    setIsSubmitting(true);
    try {
      const response = await axios.post(API_BASE_URL+"/new", newCustomer);
      setMessage(`Cliente inserito con successo!`);
      setError({});
      setNewCustomer({ surname: '', name: '', email: '', phoneNumber: '' }); // Reset del form
    } catch (error) {
      console.error('Errore nell\'inserimento del cliente:', error);
      setError(error.response?.data?.message || 'Errore sconosciuto durante l\'inserimento.');
      setMessage('');
    } finally {
        setIsSubmitting(false);
    }
  };

  return (
    <div className="container mt-4">
      <h2>Inserisci Nuovo Cliente</h2>
      {message && <div className="alert alert-success">{message}</div>}
      {error && Object.keys(error).length > 0 && (
        <div className="alert alert-danger">
          {Object.entries(error).map(([key, value]) => (
            <p key={key}>{value}</p>
          ))}
        </div>
      )}
      <form onSubmit={handleSubmit} className="mt-3">
        <div className="form-group">
          <label htmlFor="name">Nome:</label>
          <input
            type="text"
            className={`form-control ${error.name ? 'is-invalid' : ''}`}
            id="name"
            value={newCustomer.name}
            onChange={(e) => handleInputChange(e, 'name')}
            required
          />
            {error.name && <div className="invalid-feedback">{error.name}</div>}
        </div>
        <div className="form-group">
          <label htmlFor="surname">Cognome:</label>
          <input
            type="text"
            className={`form-control ${error.surname ? 'is-invalid' : ''}`}
            id="surname"
            value={newCustomer.surname}
            onChange={(e) => handleInputChange(e, 'surname')}
            required
          />
            {error.surname && <div className="invalid-feedback">{error.surname}</div>}
        </div>
        <div className="form-group">
          <label htmlFor="email">Email:</label>
          <input
            type="email"
            className={`form-control ${error.email ? 'is-invalid' : ''}`}
            id="email"
            value={newCustomer.email}
            onChange={(e) => handleInputChange(e, 'email')}
            required
          />
            {error.email && <div className="invalid-feedback">{error.email}</div>}
        </div>
        <div className="form-group">
          <label htmlFor="phoneNumber">Numero di Telefono:</label>
          <input
            type="tel"
            className={`form-control ${error.phoneNumber ? 'is-invalid' : ''}`}
            id="phoneNumber"
            value={newCustomer.phoneNumber}
            onChange={(e) => handleInputChange(e, 'phoneNumber')}
            required
            pattern="[0-9]{10}" // Esempio di pattern per 10 cifre
          />
            {error.phoneNumber && <div className="invalid-feedback">{error.phoneNumber}</div>}
        </div>
        <button type="submit" className="btn btn-primary" disabled={isSubmitting}>
            {isSubmitting ? 'Inserimento...' : 'Inserisci Cliente'}
        </button>
      </form>
    </div>
  );
};

// Componente placeholder per la modifica del cliente
const ModificaCliente = () => {
  const { emailCliente } = useParams()
  const [cliente, setCliente] = useState({
    name: '',
    surname: '',
    email: '',
    phoneNumber: '',
  });
  const [errors, setErrors] = useState({
    name: '',
    surname: '',
    email: '',
    phoneNumber: '',
  });
  const [loading, setLoading] = useState(true);
  const [submitError, setSubmitError] = useState('');
  const [submitSuccess, setSubmitSuccess] = useState('');

  useEffect(() => {
    const fetchCliente = async () => {
      setLoading(true);
      setSubmitError('');
      try {
        const response = await axios.get(`${API_BASE_URL}/singleCustomer?query=${emailCliente}`);
        setCliente(response.data);
      } catch (error) {
        setSubmitError('Errore nel caricamento dei dati del cliente.');
        console.error('Errore fetch cliente:', error);
      } finally {
        setLoading(false);
      }
    };

    if (emailCliente) {
      fetchCliente();
    }
  }, [emailCliente]);

  const validateField = (name, value) => {
    let error = '';
    switch (name) {
      case 'name':
        if (!value.trim()) {
          error = 'Il nome è obbligatorio.';
        }
        break;
      case 'surname':
        if (!value.trim()) {
          error = 'Il cognome è obbligatorio.';
        }
        break;
      case 'email':
        if (!value.trim()) {
          error = 'L\'email è obbligatoria.';
        } else if (!/\S+@\S+\.\S+/.test(value)) {
          error = 'Inserisci un\'email valida.';
        }
        break;
      case 'phoneNumber':
        if (!value.trim()) {
          error = 'Il numero di telefono è obbligatorio.';
        } else if (!/^[0-9]{10}$/.test(value)) {
          error = 'Il numero di telefono deve contenere 10 cifre.';
        }
        break;
      default:
        break;
    }
    return error;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCliente((prevState) => ({
      ...prevState,
      [name]: value,
    }));
    setErrors((prevErrors) => ({
      ...prevErrors,
      [name]: validateField(name, value),
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setSubmitError('');
    setSubmitSuccess('');

    const newErrors = {};
    for (const key in cliente) {
      newErrors[key] = validateField(key, cliente[key]);
    }
    setErrors(newErrors);

    if (Object.values(newErrors).some((error) => error !== '')) {
      setLoading(false);
      setSubmitError('Si prega di correggere gli errori nel form.');
      return;
    }

    const dataToSend = {
      prevEmail: emailCliente,
      surname: cliente.surname,
      name: cliente.name,
      role: "CUSTOMER_IN_LOCO",
      email: cliente.email,
      phoneNumber: cliente.phoneNumber,
    };

    try {
      const response = await axios.post(API_BASE_URL+"/update", dataToSend);
      console.log('Cliente modificato con successo:', response.data);
      setSubmitSuccess('Cliente modificato con successo!');
      // Puoi resettare il form o reindirizzare qui
    } catch (error) {
      setSubmitError('Errore durante la modifica del cliente.');
      console.error('Errore modifica cliente:', error);
      if (error.response && error.response.data) {
        console.error('Dettagli errore:', error.response.data);
        setSubmitError(prevError => `${prevError} ${JSON.stringify(error.response.data)}`);
      }
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="alert alert-info" role="alert">Caricamento dati cliente...</div>;
  }

  if (submitError) {
    return <div className="alert alert-danger" role="alert">Errore: {submitError}</div>;
  }

  if (submitSuccess) {
    return <div className="alert alert-success" role="alert">{submitSuccess}</div>;
  }

  return (
    <div className="container mt-4">
      <h2>Modifica Cliente</h2>
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label htmlFor="name" className="form-label">Nome:</label>
          <input
            type="text"
            className={`form-control ${errors.name ? 'is-invalid' : ''}`}
            id="name"
            name="name"
            value={cliente.name}
            onChange={handleChange}
          />
          {errors.name && <div className="invalid-feedback">{errors.name}</div>}
        </div>
        <div className="mb-3">
          <label htmlFor="surname" className="form-label">Cognome:</label>
          <input
            type="text"
            className={`form-control ${errors.surname ? 'is-invalid' : ''}`}
            id="surname"
            name="surname"
            value={cliente.surname}
            onChange={handleChange}
          />
          {errors.surname && <div className="invalid-feedback">{errors.surname}</div>}
        </div>
        <div className="mb-3">
          <label htmlFor="email" className="form-label">Email:</label>
          <input
            type="email"
            className={`form-control ${errors.email ? 'is-invalid' : ''}`}
            id="email"
            name="email"
            value={cliente.email}
            onChange={handleChange}
          />
          {errors.email && <div className="invalid-feedback">{errors.email}</div>}
        </div>
        <div className="mb-3">
          <label htmlFor="phoneNumber" className="form-label">Numero di Telefono:</label>
          <input
            type="text"
            className={`form-control ${errors.phoneNumber ? 'is-invalid' : ''}`}
            id="phoneNumber"
            name="phoneNumber"
            value={cliente.phoneNumber}
            onChange={handleChange}
          />
          {errors.phoneNumber && <div className="invalid-feedback">{errors.phoneNumber}</div>}
        </div>
        <button type="submit" className="btn btn-primary" disabled={loading}>
          {loading ? 'Salvataggio...' : 'Salva Modifiche'}
        </button>
      </form>
    </div>
  );
};


// Componente per la ricerca dei clienti
const RicercaClienti = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [customers, setCustomers] = useState([]);
  const [error, setError] = useState('');
  const [showDropdown, setShowDropdown] = useState(false); // Stato per controllare la visibilità del dropdown
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate(); // Hook per la navigazione

    const handleModifica = (emailCliente) => {
        navigate(`/modifica/${emailCliente}`);
    };

  const handleDelete = async (customerId) => {
    if (window.confirm(`Sei sicuro di voler eliminare il cliente con ID ${customerId}?`)) {
      try {
        await axios.delete(`${API_BASE_URL}/${customerId}`);
        setError('');
        fetchCustomers(); // Ricarica i clienti dopo l'eliminazione
      } catch (error) {
        console.error('Errore durante l\'eliminazione del cliente:', error);
        setError(error.response?.data?.message || 'Errore sconosciuto durante l\'eliminazione.');
      }
    }
  };

    const fetchCustomers = async () => {
        setLoading(true);
        try {
            const response = await axios.get(`${API_BASE_URL}/search?query=${searchQuery}`); // Usa un endpoint di ricerca dedicato
            setCustomers(response.data);
            setError('');
        } catch (error) {
            console.error('Errore durante la ricerca dei clienti:', error);
            setError(error.response?.data?.message || 'Errore sconosciuto durante la ricerca.');
            setCustomers([]);
        } finally {
            setLoading(false);
        }
    };

  const handleSearch = (e) => {
    e.preventDefault();
      fetchCustomers();
  };

    //Chiamata al caricamento della pagina
    useEffect(() => {
        fetchCustomers();
    }, []);

  return (
    <div className="container mt-4">
      <h2>Ricerca Clienti</h2>
      <form className="mb-3">
        <div className="form-row align-items-center">
          <div className="col-auto">
            <label className="mr-2">Cerca per:</label>
          </div>
          <div className="col-auto" style={{ position: 'relative' }}>
            <div
              className="form-control"
              onClick={() => setShowDropdown(!showDropdown)}
              style={{ cursor: 'pointer', width: '180px', textAlign: 'left' }}
            >
          
            </div>
            {showDropdown && (
              <div
                className="bg-white border rounded shadow-md"
                style={{
                  position: 'absolute',
                  zIndex: 1,
                  width: '180px',
                  marginTop: '2px',
                }}
              >
               
              </div>
            )}
          </div>
          <div className="col">
            <input
              type="text"
              className="form-control"
              placeholder={`Inserisci cognome, nome, numero di telefono o email da cercare`}
              value={searchQuery}
              onChange={(e) =>{ setSearchQuery(e.target.value);
                fetchCustomers();}
              }
              required
            />
          </div>
        
        </div>
      </form>

      {error && <div className="alert alert-danger">{error}</div>}

      {loading ? (
          <p>Caricamento risultati...</p>
      ) : (
        customers.length > 0 ? (
          <table className="table table-striped">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Cognome</th>
                <th>Email</th>
                <th>Azioni</th>
              </tr>
            </thead>
            <tbody>
              {customers.map((customer, index) => (
                <tr key={index}>
                  <td>{customer.name}</td>
                  <td>{customer.surname}</td>
                  <td>{customer.email}</td>
                  <td>
                    <button className="btn btn-primary mr-2" onClick={() => handleModifica(customer.email)}>Modifica</button>
                    <button className="btn btn-danger" onClick={() => handleDelete(customer.id)}>Elimina</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <p>Nessun cliente trovato.</p>
        )
      )}
    </div>
  );
};

// Componente principale dell'applicazione
const CustomerManager = () => {
    return (
        <Router>
            <div>
                <nav className="navbar navbar-expand-lg navbar-light bg-light">
                    <div className="container">
                        <Link className="navbar-brand" to="/">Gestione Clienti</Link>
                        <button className="navbar-toggler" type="button" data-toggle="collapse"
                                data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false"
                                aria-label="Toggle navigation">
                            <span className="navbar-toggler-icon"></span>
                        </button>
                        <div className="collapse navbar-collapse" id="navbarNav">
                            <ul className="navbar-nav">
                                <li className="nav-item">
                                    <Link className="nav-link" to="/inserisci">Inserisci Cliente</Link>
                                </li>
                                <li className="nav-item">
                                    <Link className="nav-link" to="/ricerca">Ricerca Clienti</Link>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>

                <Routes>
                    <Route path="/inserisci" element={<InserisciCliente/>}/>
                    <Route path="/ricerca" element={<RicercaClienti/>}/>
                    <Route path="/" element={<Home/>}/> {/* Pagina di default */}
                    <Route path="/modifica/:emailCliente" element={<ModificaCliente />}/>
                    {/*Definisci la rotta per il componente ModificaClientePlaceholder*/}
                </Routes>
            </div>
        </Router>
    );
};

const Home = () => {
  return (
    <div className="container mt-4">
      <h2>Benvenuto nel sistema di gestione clienti</h2>
      <p>Utilizzare i link nel menù di navigazione per inserire o ricercare i clienti</p>
    </div>
  );
};

export default CustomerManager;