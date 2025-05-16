import React, { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css'; // Assicurati che Bootstrap sia installato e importato

// URL base per le chiamate API (sostituiscilo con il tuo endpoint reale)
const API_BASE_URL = 'http://localhost:8080'; // !!! SOSTITUISCI QUESTO CON IL TUO URL REALE !!!

// Le funzioni API helper che gestiscono le chiamate Axios.
// Le implementazioni specifiche del catch verranno gestite a livello di componente
// per poter impostare lo stato 'error'. Qui definiamo solo le chiamate pure.
const api = {
    fetchTodayReservations: async () => {
        // Esempio: GET /api/reservations/today
        const response = await axios.get(`${API_BASE_URL}/api/reservation/today`);
        return response.data;
    },
    searchCustomers: async (input) => {
        if (!input) return [];
        // Esempio: GET /api/customers/search?query=<input>
        const response = await axios.get(`${API_BASE_URL}/api/customer/search`, { params: { query: input } });
        return response.data; // Dovrebbe ritornare List<CustomerDtoListProjection>
    },
    fetchCustomerReservations: async (email) => {
        // Esempio: GET /api/reservations/by-customer?email=<email>
        const response = await axios.get(`${API_BASE_URL}/api/reservation/customerRes`, { params: { email: email } });
        return response.data; // Dovrebbe ritornare List<ReservationDto>
    },
    fetchServiceNames: async () => {
        // Esempio: GET /api/services/names
        const response = await axios.get(`${API_BASE_URL}/api/service/getServices`);
        return response.data; // Dovrebbe ritornare List<String>
    },
    fetchServiceReservations: async (serviceName) => {
        // Esempio: GET /api/reservations/by-service?serviceName=<serviceName>
        const response = await axios.get(`${API_BASE_URL}/api/reservation/serviceRes`, { params: { serviceName: serviceName } });
        return response.data; // Dovrebbe ritornare List<ReservationDto>
    },
    fetchDateRangeReservations: async (startDate, endDate) => {
        // Esempio: GET /api/reservations/by-date-range?startDate=<startDate>&endDate=<endDate>
        const response = await axios.get(`${API_BASE_URL}/api/reservation/findByDate`, { params: { startDate: startDate, endDate: endDate } });
        return response.data; // Dovrebbe ritornare List<ReservationDto>
    },
    deleteReservation: async (id) => {
        // Esempio: DELETE /api/reservations/<id>
        await axios.delete(`${API_BASE_URL}/api/reservation/${id}`);
    }
};

// Definizione dei tipi per chiarezza (opzionale ma consigliato in TypeScript)
// In JS, servono solo come riferimento
/*
type ReservationDto = {
    id: number;
    date: string; // Formato ISO Coppola -MM-DD
    serviceName: string;
    name: string;
    surname: string;
    hour: number; // Orario in formato 24h (e.g., 15 per 15:00)
};

type CustomerDtoListProjection = {
    surname: string;
    name: string;
    email: string;
};
*/


const SearchReservations = () => {
    const [reservations, setReservations] = useState([]);
    const [searchType, setSearchType] = useState('today'); // 'today', 'customer', 'service', 'dateRange'
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null); // Stato per il messaggio di errore

    // State per ricerca per Cliente
    const [customerSearchInput, setCustomerSearchInput] = useState('');
    const [customerSuggestions, setCustomerSuggestions] = useState([]);
    const [selectedCustomerEmail, setSelectedCustomerEmail] = useState(null);

    // State per ricerca per Servizio
    const [serviceOptions, setServiceOptions] = useState([]);
    const [selectedService, setSelectedService] = useState('');

    // State per ricerca per Data
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');

     // Funzione helper per gestire gli errori delle chiamate API e impostare lo stato 'error' del componente
     const handleComponentError = useCallback((error, defaultMessage) => {
        let errorMessage = defaultMessage;

        if (axios.isAxiosError(error)) {
            if (error.response) {
                // La richiesta è stata fatta e il server ha risposto con uno status code
                // che non rientra nel range 2xx
                errorMessage += ` Stato: ${error.response.status}.`;
                 if (error.response.data && (error.response.data.message || typeof error.response.data === 'string')) {
                    // Preferisci un campo 'message' o l'intero corpo se è una stringa
                    errorMessage += ` Dettaglio: ${error.response.data.message || error.response.data}`;
                }
                // Puoi aggiungere logica specifica per status code comuni
                if (error.response.status === 404) errorMessage = "Risorsa non trovata.";
                else if (error.response.status === 400) errorMessage = "Richiesta non valida.";
                else if (error.response.status >= 500) errorMessage = "Errore interno del server.";

            } else if (error.request) {
                // La richiesta è stata fatta ma non è stata ricevuta alcuna risposta
                errorMessage = "Errore di rete: Impossibile raggiungere il server. Controlla la connessione.";
            } else {
                // Qualcosa è successo nell'impostare la richiesta che ha attivato un Errore
                errorMessage = `Errore nella richiesta: ${error.message}`;
            }
        } else {
            // Errore non-Axios (es. errore nel codice JavaScript prima della chiamata API)
            errorMessage = `Si è verificato un errore inatteso: ${error instanceof Error ? error.message : String(error)}`;
        }

         console.error("Errore gestito:", error); // Log dettagliato in console per debug
        setError(errorMessage); // Imposta lo stato di errore nel componente
         setReservations([]); // Pulisce i risultati in caso di errore di caricamento/ricerca
    }, []); // Questa funzione non dipende da stati o props del componente che cambiano frequentemente, quindi può essere memoizzata

    // Funzione helper per fetchare e settare le prenotazioni con gestione loading/error
    const fetchAndSetReservations = useCallback(async (fetchApiCall) => {
         setLoading(true);
         setError(null); // Resetta l'errore
         setReservations([]); // Pulisci i risultati precedenti
         try {
             const data = await fetchApiCall(); // Chiama la funzione API pura
             setReservations(data); // Imposta i dati se la chiamata ha successo
         } catch (err) {
             // Cattura l'errore e chiama l'handler specifico del componente
             handleComponentError(err, "Errore nel recupero delle prenotazioni.");
         } finally {
             setLoading(false);
         }
    }, [handleComponentError]); // Dipende dalla funzione handleComponentError memoizzata

     // Funzione helper per fetchare i nomi dei servizi con gestione loading/error
    const fetchServiceNamesWrapped = useCallback(async () => {
        setLoading(true);
        setError(null); // Resetta l'error
        setServiceOptions([]); // Pulisci opzioni precedenti
        try {
            const names = await api.fetchServiceNames(); // Chiama la funzione API pura
            setServiceOptions(names); // Imposta i nomi se la chiamata ha successo
        } catch (err) {
             handleComponentError(err, "Impossibile caricare la lista dei servizi.");
        } finally {
            setLoading(false);
        }
    }, [handleComponentError]);


    // Effetto per caricare le prenotazioni del giorno all'apertura
    useEffect(() => {
         // Usa la funzione helper per caricare le prenotazioni odierne
         fetchAndSetReservations(api.fetchTodayReservations);
         setSearchType('today'); // Assicura che il selettore sia su 'today'

         // Pulisci l'errore quando il componente viene smontato
         return () => {
            setError(null);
         };
    }, [fetchAndSetReservations]); // Dipende da fetchAndSetReservations


    // Funzione per resettare gli stati specifici delle ricerche
    const resetSearchStates = useCallback(() => {
        setCustomerSearchInput('');
        setCustomerSuggestions([]);
        setSelectedCustomerEmail(null);
        setServiceOptions([]);
        setSelectedService('');
        setStartDate('');
        setEndDate('');
        setReservations([]); // Pulisce i risultati precedenti
        setError(null); // !!! Resetta l'errore quando si cambia tipo di ricerca !!!
    }, []); // Non ha dipendenze esterne


    // Gestore cambio tipo di ricerca
    const handleSearchTypeChange = useCallback((event) => {
        const type = event.target.value;
        setSearchType(type);
        resetSearchStates(); // Resetta gli stati e l'errore

        // Carica i dati iniziali se necessario per il nuovo tipo
        if (type === 'today') {
            fetchAndSetReservations(api.fetchTodayReservations);
        } else if (type === 'service') {
             fetchServiceNamesWrapped();
        }
    }, [resetSearchStates, fetchAndSetReservations, fetchServiceNamesWrapped]);


    // Gestione input ricerca cliente (con debounce/throttle potenziale)
    const handleCustomerSearchInputChange = useCallback((event) => {
        const input = event.target.value;
        setCustomerSearchInput(input);
        setSelectedCustomerEmail(null); // Deseleziona il cliente se l'input cambia
        setError(null); // Pulisce l'errore potenziale di una ricerca precedente fallita

        if (input.length > 2) { // Esegue la ricerca solo se l'input ha almeno 3 caratteri
            // Gestione errori console-only per i suggerimenti
            api.searchCustomers(input)
               .then(suggestions => setCustomerSuggestions(suggestions))
               .catch(err => {
                   console.error("Errore nella ricerca clienti (suggestions):", err);
                   setCustomerSuggestions([]); // Assicurati di pulire i suggerimenti anche in caso di errore
               });
        } else {
            setCustomerSuggestions([]); // Pulisce i suggerimenti se l'input è corto
        }
    }, []); // Nessuna dipendenza esterna


    // Selezione suggerimento cliente
    const handleCustomerSelect = useCallback((customer) => {
        setCustomerSearchInput(`${customer.name} ${customer.surname} (${customer.email})`);
        setSelectedCustomerEmail(customer.email);
        setCustomerSuggestions([]); // Pulisce i suggerimenti dopo la selezione
        setError(null); // Pulisce l'errore potenziale

        // Esegue la ricerca delle prenotazioni del cliente usando la funzione helper
        fetchAndSetReservations(() => api.fetchCustomerReservations(customer.email));
    }, [fetchAndSetReservations]);


    // Selezione servizio
    const handleServiceSelect = useCallback((event) => {
        const service = event.target.value;
        setSelectedService(service);
        setError(null); // Pulisce l'errore precedente
        setReservations([]); // Pulisce i risultati precedenti mentre carica i nuovi

        if (service) {
            // Esegue la ricerca delle prenotazioni per servizio usando la funzione helper
            fetchAndSetReservations(() => api.fetchServiceReservations(service));
        }
    }, [fetchAndSetReservations]);


    // Gestione cambio data inizio
    const handleStartDateChange = useCallback((event) => {
        setStartDate(event.target.value);
        setError(null); // Pulisce l'errore quando si cambiano le date
        setReservations([]); // Pulisce i risultati
    }, []);

    // Gestione cambio data fine
    const handleEndDateChange = useCallback((event) => {
        setEndDate(event.target.value);
        setError(null); // Pulisce l'errore quando si cambiano le date
        setReservations([]); // Pulisce i risultati
    }, []);


    // Esecuzione ricerca per data
    const handleDateRangeSearch = useCallback(() => {
         setError(null); // Resetta l'errore
         setReservations([]); // Pulisce i risultati

        if (!startDate || !endDate) {
            setError("Seleziona sia la data di inizio che quella di fine.");
            return; // Blocca l'esecuzione se le date non sono selezionate
        }

         // Validazione client-side delle date
         const start = new Date(startDate);
         const end = new Date(endDate);
         if (start > end) {
             setError("La data di inizio non può essere successiva alla data di fine.");
             return; // Blocca l'esecuzione se il range non è valido
         }

        // Esegue la ricerca delle prenotazioni per data usando la funzione helper
        fetchAndSetReservations(() => api.fetchDateRangeReservations(startDate, endDate));
    }, [startDate, endDate, fetchAndSetReservations]);


    // Gestione eliminazione prenotazione
    const handleDeleteReservation = useCallback(async (id) => {
        setLoading(true);
        setError(null); // Resetta l'errore prima dell'operazione
        try {
            // Chiama la funzione API pura e gestisce l'errore qui
            await api.deleteReservation(id);

            // Se l'API ha successo, rimuove la prenotazione dalla lista locale
            setReservations(prevReservations => prevReservations.filter(res => res.id !== id));

        } catch (err) {
            // Gestione dell'errore specifica per l'eliminazione
            handleComponentError(err, `Errore nell'eliminazione della prenotazione ${id}.`);
        } finally {
            setLoading(false);
        }
    }, [handleComponentError]);


    return (
        <div className="container mt-4">
            <h2 className="mb-4">Ricerca Prenotazioni</h2>

            <div className="mb-3 row align-items-center">
                <label htmlFor="searchTypeSelect" className="col-sm-3 col-form-label">Seleziona tipo ricerca:</label>
                <div className="col-sm-9">
                    <select
                        id="searchTypeSelect"
                        className="form-select"
                        value={searchType}
                        onChange={handleSearchTypeChange}
                        disabled={loading}
                    >
                        <option value="today">Prenotazioni odierne</option>
                        <option value="customer">Per Cliente</option>
                        <option value="service">Per Servizio</option>
                        <option value="dateRange">Per Periodo</option>
                    </select>
                </div>
            </div>

            {/* Input dinamico basato sul tipo di ricerca */}
            {searchType === 'customer' && (
                <div className="mb-3 row align-items-center">
                     <label htmlFor="customerSearchInput" className="col-sm-3 col-form-label">Cerca Cliente:</label>
                     <div className="col-sm-9">
                        <input
                            type="text"
                            id="customerSearchInput"
                            className="form-control"
                            placeholder="Inserisci nome o cognome del cliente"
                            value={customerSearchInput}
                            onChange={handleCustomerSearchInputChange}
                            disabled={loading}
                        />
                         {/* Mostra suggerimenti solo se l'input ha testo, non c'è loading, e ci sono suggerimenti */}
                         {customerSearchInput.length > 0 && customerSuggestions.length > 0 && !loading && (
                            <ul className="list-group mt-1" style={{ position: 'absolute', zIndex: 1000, width: 'calc(100% - 15px)' }}>
                                {customerSuggestions.map((customer, index) => (
                                    <li
                                        key={customer.email || index} // Usa email come key se disponibile e unica
                                        className="list-group-item list-group-item-action"
                                        onClick={() => handleCustomerSelect(customer)}
                                        style={{ cursor: 'pointer' }}
                                    >
                                        {customer.name} {customer.surname} ({customer.email})
                                    </li>
                                ))}
                            </ul>
                        )}
                         {/* Mostra il cliente selezionato se non ci sono suggerimenti attivi e l'input ha testo */}
                         {selectedCustomerEmail && customerSuggestions.length === 0 && customerSearchInput.length > 0 && (
                             <p className="form-text text-muted mt-2">Cliente selezionato: {customerSearchInput}</p>
                         )}
                    </div>
                </div>
            )}

            {searchType === 'service' && (
                <div className="mb-3 row align-items-center">
                    <label htmlFor="serviceSelect" className="col-sm-3 col-form-label">Seleziona Servizio:</label>
                    <div className="col-sm-9">
                        <select
                            id="serviceSelect"
                            className="form-select"
                            value={selectedService}
                            onChange={handleServiceSelect}
                            disabled={loading || serviceOptions.length === 0}
                        >
                            <option value="">-- Seleziona un servizio --</option>
                            {serviceOptions.map((service, index) => (
                                <option key={index} value={service}>{service}</option>
                            ))}
                        </select>
                         {/* Mostra messaggio caricamento servizi solo quando necessario */}
                         {loading && searchType === 'service' && serviceOptions.length === 0 && !error && (
                            <small className="text-muted">Caricamento servizi...</small>
                         )}
                         {/* Potresti voler mostrare un messaggio se non ci sono servizi disponibili */}
                         {!loading && searchType === 'service' && serviceOptions.length === 0 && !error && selectedService === '' && (
                             <small className="text-muted">Nessun servizio disponibile.</small>
                         )}
                    </div>
                </div>
            )}

            {searchType === 'dateRange' && (
                <div className="mb-3 row align-items-center">
                    <label className="col-sm-3 col-form-label">Seleziona Periodo:</label>
                    <div className="col-sm-4">
                        <label htmlFor="startDateInput" className="visually-hidden">Data Inizio</label>
                        <input
                            type="date"
                            id="startDateInput"
                            className="form-control"
                            value={startDate}
                            onChange={handleStartDateChange}
                            disabled={loading}
                        />
                    </div>
                     <div className="col-sm-4">
                        <label htmlFor="endDateInput" className="visually-hidden">Data Fine</label>
                         <input
                            type="date"
                            id="endDateInput"
                            className="form-control"
                            value={endDate}
                            onChange={handleEndDateChange}
                            disabled={loading}
                        />
                    </div>
                    <div className="col-sm-1">
                         <button
                             className="btn btn-primary"
                             onClick={handleDateRangeSearch}
                             disabled={loading || !startDate || !endDate}
                         >
                             Cerca
                         </button>
                     </div>
                </div>
            )}

            {/* Visualizzazione Loading */}
            {loading && (
                <div className="d-flex justify-content-center mt-4">
                    <div className="spinner-border text-primary" role="status">
                        <span className="visually-hidden">Caricamento...</span>
                    </div>
                </div>
            )}

            {/* Visualizzazione Error */}
            {error && (
                <div className="alert alert-danger mt-4" role="alert">
                    {error}
                     {/* Opzione per chiudere l'alert - non richiesta ma utile */}
                     {/* <button type="button" className="btn-close" aria-label="Close" onClick={() => setError(null)}></button> */}
                </div>
            )}

            {/* Tabella dei risultati */}
            {/* Mostra la tabella solo se non c'è loading, non c'è error, E ci sono prenotazioni */}
            {!loading && !error && reservations.length > 0 && (
                <div className="mt-4">
                    <h4>Risultati Ricerca</h4>
                    <div className="table-responsive">
                         <table className="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Data</th>
                                    <th>Servizio</th>
                                    <th>Nome</th>
                                    <th>Cognome</th>
                                    <th>Ora</th>
                                    <th>Azioni</th>
                                </tr>
                            </thead>
                            <tbody>
                                {reservations.map((reservation) => (
                                    <tr key={reservation.id}>
                                        <td>{reservation.id}</td>
                                        <td>{reservation.date}</td> {/* Assicurati che il formato data sia leggibile */}
                                        <td>{reservation.serviceName}</td>
                                        <td>{reservation.name}</td>
                                        <td>{reservation.surname}</td>
                                        <td>{String(reservation.hour).padStart(2, '0')}:00</td> {/* Formatta l'ora come HH:00 */}
                                        <td>
                                            <button
                                                className="btn btn-danger btn-sm"
                                                onClick={() => handleDeleteReservation(reservation.id)}
                                                disabled={loading} // Disabilita il bottone elimina durante il caricamento
                                            >
                                                Elimina
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            )}

             {/* Messaggio se non ci sono risultati */}
             {/* Mostra se non c'è loading, non c'è error, e la lista prenotazioni è vuota */}
             {!loading && !error && reservations.length === 0 && (
                  // Messaggio leggermente diverso a seconda del tipo di ricerca attivo
                  <div className="mt-4 alert alert-info">
                     {searchType === 'today' && "Nessuna prenotazione trovata per oggi."}
                     {searchType === 'customer' && (selectedCustomerEmail ? "Nessuna prenotazione trovata per questo cliente." : "Cerca un cliente per visualizzare le sue prenotazioni.")}
                     {searchType === 'service' && (selectedService ? "Nessuna prenotazione trovata per questo servizio." : "Seleziona un servizio per visualizzare le prenotazioni.")}
                     {searchType === 'dateRange' && (startDate && endDate ? "Nessuna prenotazione trovata nel periodo selezionato." : "Seleziona un periodo per visualizzare le prenotazioni.")}
                     {!['today', 'customer', 'service', 'dateRange'].includes(searchType) && "Nessuna prenotazione trovata."} {/* Fallback */}
                  </div>
             )}

        </div>
    );
};

export default SearchReservations;