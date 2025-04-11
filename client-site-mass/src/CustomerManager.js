import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { BrowserRouter as Router, Routes, Route, Link, useNavigate } from 'react-router-dom';
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
      const response = await axios.post(API_BASE_URL, newCustomer);
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
const ModificaClientePlaceholder = ({ customerId }) => {
  // Questo componente prenderà il posto del tuo componente di modifica.
  // In realtà dovresti recuperare i dati del cliente con l'ID customerId e
  // mostrare un form di modifica.  Per semplicità, qui mostriamo solo un messaggio.

  return (
    <div className="container mt-4">
      <h2>Modifica Cliente</h2>
      <p>Stai modificando il cliente con ID: {customerId}</p>
      {/* Qui andrebbe il form di modifica del cliente */}
    </div>
  );
};

// Componente per la ricerca dei clienti
const RicercaClienti = () => {
  const [searchType, setSearchType] = useState('surname');
  const [searchQuery, setSearchQuery] = useState('');
  const [customers, setCustomers] = useState([]);
  const [error, setError] = useState('');
  const [showDropdown, setShowDropdown] = useState(false); // Stato per controllare la visibilità del dropdown
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate(); // Hook per la navigazione

    const handleModifica = (customerId) => {
        navigate(`/modifica/${customerId}`);
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
            const response = await axios.get(`${API_BASE_URL}/search?type=${searchType}&query=${searchQuery}`); // Usa un endpoint di ricerca dedicato
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

    const searchTypes = [
        { value: 'surname', label: 'Cognome' },
        { value: 'name', label: 'Nome' },
        { value: 'phoneNumber', label: 'Numero di Telefono' },
        { value: 'email', label: 'Email' },
    ];

  return (
    <div className="container mt-4">
      <h2>Ricerca Clienti</h2>
      <form onSubmit={handleSearch} className="mb-3">
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
              {searchTypes.find(type => type.value === searchType)?.label || 'Tipo di ricerca'}
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
                {searchTypes.map(type => (
                  <div
                    key={type.value}
                    onClick={() => {
                      setSearchType(type.value);
                      setShowDropdown(false);
                    }}
                    className="px-2 py-1 hover:bg-light"
                    style={{ cursor: 'pointer' }}
                  >
                    {type.label}
                  </div>
                ))}
              </div>
            )}
          </div>
          <div className="col">
            <input
              type="text"
              className="form-control"
              placeholder={`Inserisci ${searchType === 'surname' ? 'cognome' : searchType === 'name' ? 'nome' : searchType === 'phoneNumber' ? 'numero di telefono' : 'email'} da cercare`}
              value={searchQuery}
              onChange={(e) =>{ setSearchQuery(e.target.value);
                fetchCustomers();}
              }
              required
            />
          </div>
          <div className="col-auto">
            <button type="submit" className="btn btn-primary" disabled={loading}>
                {loading ? 'Ricerca...' : 'Cerca'}
            </button>
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
                    <button className="btn btn-primary mr-2" onClick={() => handleModifica(customer.id)}>Modifica</button>
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
                    <Route path="/modifica/:customerId" element={<ModificaClientePlaceholder customerId={""}/>}/>
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