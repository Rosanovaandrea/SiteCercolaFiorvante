import React, { useState,useEffect } from 'react';
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080'; // e.g., 'http://localhost:8080/api'

const apiClient = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
        // Add any necessary auth headers here, e.g., 'Authorization': `Bearer ${yourAuthToken}`
    },
});

// --- Actual API Calls using Axios ---

const fetchCustomers = async (searchTerm) => {
  console.log('Calling API to search customers for:', searchTerm);
  try {
    const response = await apiClient.get('api/customer/search?query='+searchTerm);
    console.log('Customers API response:', response.data);
    return response.data; // Axios puts the response body in .data
  } catch (error) {
    console.error('API Error fetching customers:', error);
    throw error; // Re-throw the error for the component to handle
  }
};

const fetchServices = async (searchTerm) => {
  console.log('Calling API to search services for:', searchTerm);
  try {
    const response = await apiClient.get('/api/service/getServices');
    console.log('Services API response:', response.data);
    return response.data; // Should be an array of service name strings
  } catch (error) {
    console.error('API Error fetching services:', error);
    throw error; // Re-throw the error for the component to handle
  }
};

const createReservation = async (reservation) => {
    console.log('Calling API to create reservation with data:', reservation);
    try {
        const response = await apiClient.post('/api/reservation/insertReservation', reservation);
        console.log('Create Reservation API response:', response.data);
        return response.data; // Or response.status if you only need to confirm success
    } catch (error) {
        console.error('API Error creating reservation:', error);
        throw error; // Re-throw the error for the component to handle
    }
};

// --- React Component ---

const InsertReservation = () => {
  const [currentStep, setCurrentStep] = useState(1);
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [selectedService, setSelectedService] = useState(null);
  const [selectedDate, setSelectedDate] = useState('');
  const [selectedTimeSlot, setSelectedTimeSlot] = useState(''); // Store as string initially from select

  const [customerSearchTerm, setCustomerSearchTerm] = useState('');
  const [customerSearchResults, setCustomerSearchResults] = useState([]);
  const [isLoadingCustomers, setIsLoadingCustomers] = useState(false);
  const [customerError, setCustomerError] = useState(null); // Error specific to customer search
  const [serviceSearchResults, setServiceSearchResults] = useState([]);
  const [isLoadingServices, setIsLoadingServices] = useState(false);
  const [serviceError, setServiceError] = useState(null); // Error specific to service search

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState(null); // Error specific to final submission
  const [submitSuccess, setSubmitSuccess] = useState(null); // Success message after final submission


  // --- Effect for initial customer search and search term changes ---
  useEffect(() => {
      const searchCustomers = async () => {
          setIsLoadingCustomers(true);
          setCustomerError(null); // Clear previous errors
          // setCustomerSearchResults([]); // Optional: Clear results immediately on search
          try {
            if(customerSearchTerm){
              const results = await fetchCustomers(customerSearchTerm); // Use the actual API call
              setCustomerSearchResults(results);
            }
          } catch (err) {
              console.error('Error fetching customers:', err);
              // Axios errors have different properties depending on the error type
              const errorMessage = err.response?.data?.message || err.message || 'Impossibile cercare i clienti. Riprova più tardi.';
              setCustomerError(errorMessage);
               setCustomerSearchResults([]); // Clear results on error
          } finally {
              setIsLoadingCustomers(false);
          }
      };
      // Add a small delay before searching to avoid excessive calls while typing
      const handler = setTimeout(() => {
           searchCustomers();
      }, 300); // 300ms debounce delay

      // Cleanup function to clear the timeout if the component unmounts or search term changes
      return () => {
          clearTimeout(handler);
      };

  }, [customerSearchTerm]); // Dependency array: re-run effect when customerSearchTerm changes


  const handleCustomerSelect = (customer) => {
    setSelectedCustomer(customer);
    setCustomerSearchResults([]); // Clear results after selection
    setCustomerSearchTerm(''); // Clear search term
    setCustomerError(null); // Clear any customer errors
    setCurrentStep(2);
  };

  // --- Effect for initial service search and search term changes ---
   useEffect(() => {
      if (currentStep !== 2) return; // Only search services if we are at step 2 or beyond

      const searchServices = async () => {
          setIsLoadingServices(true);
          setServiceError(null); // Clear previous errors
          setServiceSearchResults([]);
          try {
              const results = await fetchServices(); // Use the actual API call
              setServiceSearchResults(results);
          } catch (err) {
              console.error('Error fetching services:', err);
               const errorMessage = err.response?.data?.message || err.message || 'Impossibile cercare i servizi. Riprova più tardi.';
              setServiceError(errorMessage);
              setServiceSearchResults([]); // Clear results on error
          } finally {
              setIsLoadingServices(false);
          }
      };

      searchServices();

  }, [currentStep]); // Re-run when serviceSearchTerm changes or we enter step 2


  const handleServiceSelect = (serviceName) => {
    setSelectedService(serviceName);
    setServiceSearchResults([]);  // Clear search term
    setServiceError(null); // Clear any service errors
    setCurrentStep(2);
  };

  // --- Step 3: Date and Time Selection ---

  const handleDateChange = (e) => {
    setSelectedDate(e.target.value);
  };

  const handleTimeSlotChange = (e) => {
    setSelectedTimeSlot(e.target.value);
  };

  // --- Step 4: Review and Submit ---

  const handleSubmit = async () => {
      setSubmitError(null); // Clear previous errors
      setSubmitSuccess(null); // Clear previous success messages

      if (!selectedCustomer || !selectedService || !selectedDate || selectedTimeSlot === '') {
          setSubmitError("Per favore, completa tutti i campi obbligatori prima di inviare."); // Basic client-side validation message
          return;
      }

      const reservation = {
          email: selectedCustomer.email, // Get email from selected customer
          serviceName: selectedService,
          date: selectedDate, // date is already in 'YYYY-MM-DD' format from input type="date"
          hour: parseInt(selectedTimeSlot, 10), // Parse slot number to integer
      };

      setIsSubmitting(true);
      try {
          const response = await createReservation(reservation); // Use the actual API call
          console.log('Reservation successful response:', response.data);
          // Assuming backend returns some data on success, or check response.status like 201
          setSubmitSuccess('Prenotazione creata con successo!'); // Show success message
          // Optionally reset form after success or navigate
          // resetForm();
      } catch (err) {
          console.error('Error submitting reservation:', err);
           // Display a user-friendly error message
          // Check for response data message, then generic error message, then fallback
          const errorMessage = err.response?.data?.message || err.message || 'Errore durante la creazione della prenotazione. Riprova.';
          setSubmitError(errorMessage);
      } finally {
          setIsSubmitting(false);
      }
  };

  const resetForm = () => {
      setCurrentStep(1);
      setSelectedCustomer(null);
      setSelectedService(null);
      setSelectedDate('');
      setSelectedTimeSlot('');
      setCustomerSearchTerm('');
      setCustomerSearchResults([]);
      setServiceSearchResults([]);
      setSubmitError(null);
      setSubmitSuccess(null);
  };

  // --- Navigation ---

  const nextStep = () => {
      // Basic validation before moving
      if (currentStep === 1 && !selectedCustomer) return;
      if (currentStep === 2 && !selectedService) return;
      if (currentStep === 3 && (!selectedDate || selectedTimeSlot === '')) return;

       // Clear errors from the step we are leaving
      if(currentStep === 1) setCustomerError(null);
      if(currentStep === 2) setServiceError(null);
      if(currentStep === 3) {
          setSubmitError(null); // Clear submit errors if going back to step 3
          setSubmitSuccess(null); // Clear submit success if going back to step 3
      }


      setCurrentStep(currentStep + 1);
  };

  const prevStep = () => {
      // Clear errors from the step we are leaving
      if(currentStep === 2) setCustomerError(null);
      if(currentStep === 3) setServiceError(null);
      if(currentStep === 4) {
          setSubmitError(null); // Clear submit errors when going back from step 4
          setSubmitSuccess(null); // Clear submit success when going back from step 4
      }
      setCurrentStep(currentStep - 1);
  };

    // --- Render Logic ---

  const renderStep = () => {
    switch (currentStep) {
      case 1:
        return (
          <div className="step-content-inner">
            <h2 className="mb-4">Step 1: Seleziona Cliente</h2>
            <div className="mb-3">
              <label htmlFor="customer-search" className="form-label">Cerca Cliente</label>
              <input
                type="text"
                className="form-control"
                id="customer-search"
                placeholder="Nome, cognome, email o telefono"
                value={customerSearchTerm}
                onChange={(e) => setCustomerSearchTerm(e.target.value)}
              />
            </div>

            {/* Loading and Error messages */}
            {isLoadingCustomers && <div className="text-center my-3"><div className="spinner-border text-primary" role="status"><span className="visually-hidden">Caricamento...</span></div></div>}
            {customerError && <div className="alert alert-danger mt-3">{customerError}</div>}

            {/* Search Results List */}
            {!isLoadingCustomers && !customerError && customerSearchResults.length > 0 && (
                <div className="mt-3">
                    <h5>Risultati Ricerca:</h5>
                    <ul className="list-group">
                      {customerSearchResults.map(customer => (
                        // Assuming customer.email is unique for the key
                        <li
                            key={customer.email}
                            onClick={() => handleCustomerSelect(customer)}
                            className="list-group-item list-group-item-action"
                            style={{ cursor: 'pointer' }} // Add cursor style
                        >
                          {customer.name} {customer.surname} ({customer.email}{customer.phone ? `, Tel: ${customer.phone}` : ''})
                        </li>
                      ))}
                    </ul>
                </div>
            )}
            {/* No results message */}
            {!isLoadingCustomers && !customerError && customerSearchResults.length === 0 && customerSearchTerm && (
                 <div className="alert alert-info mt-3">Nessun cliente trovato per "{customerSearchTerm}". Prova una ricerca diversa.</div>
            )}
            {/* Initial prompt message */}
             {!isLoadingCustomers && !customerError && customerSearchResults.length === 0 && !customerSearchTerm && (
                 <div className="alert alert-info mt-3">Inizia a digitare per cercare i clienti esistenti.</div>
            )}


            {/* Selected Customer Summary */}
            {selectedCustomer && (
                <div className="card mt-4">
                    <div className="card-body">
                        <h5 className="card-title">Cliente Selezionato:</h5>
                        <p className="card-text"><strong>Nome:</strong> {selectedCustomer.name}</p>
                        <p className="card-text"><strong>Cognome:</strong> {selectedCustomer.surname}</p>
                        <p className="card-text"><strong>Email:</strong> {selectedCustomer.email}</p>
                         {/* Add phone if your DTO includes it and you display it */}
                    </div>
                </div>
            )}
          </div>
        );
      case 2:
        return (
          <div className="step-content-inner">
            <h2 className="mb-4">Step 2: Seleziona Servizio</h2>

             {/* Loading and Error messages */}
            {isLoadingServices && <div className="text-center my-3"><div className="spinner-border text-primary" ><span className="visually-hidden">Caricamento...</span></div></div>}
            {serviceError && <div className="alert alert-danger mt-3">{serviceError}</div>}

             {/* Search Results List */}
            {!isLoadingServices && !serviceError && serviceSearchResults.length > 0 && (
                 <div className="mt-3">
                    <h5>Risultati Ricerca:</h5>
                    <ul className="list-group">
                      {/* Assuming service names are unique for the key */}
                      {serviceSearchResults.map(serviceName => (
                        <li
                            key={serviceName}
                            onClick={() => handleServiceSelect(serviceName)}
                            className="list-group-item list-group-item-action"
                             style={{ cursor: 'pointer' }} // Add cursor style
                        >
                          {serviceName}
                        </li>
                      ))}
                    </ul>
                 </div>
            )}
              {/* No results message */}
              {!isLoadingServices && !serviceError && serviceSearchResults.length === 0 && (
                 <div className="alert alert-info mt-3">Nessun servizio trovato ".</div>
            )}
            


             {/* Selected Service Summary */}
             {selectedService && (
                <div className="card mt-4">
                     <div className="card-body">
                        <h5 className="card-title">Servizio Selezionato:</h5>
                        <p className="card-text">{selectedService}</p>
                     </div>
                </div>
            )}
          </div>
        );
      case 3:
        return (
          <div className="step-content-inner">
            <h2 className="mb-4">Step 3: Seleziona Data e Orario</h2>
            <div className="mb-3">
              <label htmlFor="reservation-date" className="form-label">Data:</label>
              <input
                type="date"
                className="form-control"
                id="reservation-date"
                value={selectedDate}
                onChange={handleDateChange}
                 // Optional: Set min date to today
                 min={new Date().toISOString().split('T')[0]}
              />
            </div>
            <div className="mb-3">
              <label htmlFor="reservation-time" className="form-label">Orario (Slot da 1 ora):</label>
              <select
                className="form-select"
                id="reservation-time"
                value={selectedTimeSlot}
                onChange={handleTimeSlotChange}
              >
                <option value="" disabled>Seleziona uno slot</option>
                {[...Array(8)].map((_, index) => (
                  // Map index 0-7 to user-friendly display like Slot 1 (08:00 - 09:00) if desired
                  // For this example, just using Slot number + 1
                  <option key={index} value={index}>{`Slot ${index + 1}`}</option>
                ))}
              </select>
            </div>

            {/* Selected Date and Time Summary */}
             {(selectedDate || selectedTimeSlot !== '') && (
                <div className="card mt-4">
                    <div className="card-body">
                        <h5 className="card-title">Dettagli Selezionati:</h5>
                        {selectedDate && <p className="card-text"><strong>Data:</strong> {selectedDate}</p>}
                        {selectedTimeSlot !== '' && <p className="card-text"><strong>Orario:</strong> Slot {parseInt(selectedTimeSlot, 10) + 1}</p>}
                    </div>
                </div>
            )}
          </div>
        );
      case 4:
        return (
          <div className="step-content-inner">
            <h2 className="mb-4">Step 4: Riepilogo Prenotazione</h2>

            {/* Submission Feedback Messages */}
            {submitError && <div className="alert alert-danger mb-3">{submitError}</div>}
            {submitSuccess && <div className="alert alert-success mb-3">{submitSuccess}</div>}

            {/* Summaries */}
            {selectedCustomer && (
              <div className="card mb-3">
                <div className="card-body">
                  <h5 className="card-title">Cliente:</h5>
                  <p className="card-text"><strong>Nome:</strong> {selectedCustomer.name}</p>
                  <p className="card-text"><strong>Cognome:</strong> {selectedCustomer.surname}</p>
                  <p className="card-text"><strong>Email:</strong> {selectedCustomer.email}</p>
                  {/* Add phone if your DTO includes it and you display it */}
                </div>
              </div>
            )}
            {selectedService && (
               <div className="card mb-3">
                 <div className="card-body">
                  <h5 className="card-title">Servizio:</h5>
                  <p className="card-text">{selectedService}</p>
                 </div>
               </div>
            )}
            {(selectedDate || selectedTimeSlot !== '') && (
               <div className="card mb-3">
                <div className="card-body">
                  <h5 className="card-title">Data e Orario:</h5>
                  {selectedDate && <p className="card-text"><strong>Data:</strong> {selectedDate}</p>}
                  {selectedTimeSlot !== '' && <p className="card-text"><strong>Orario:</strong> Slot {parseInt(selectedTimeSlot, 10) + 1}</p>}
                </div>
               </div>
            )}

             {/* Instructions or further info for Step 4 */}
             {!submitError && !submitSuccess && !isSubmitting && (
                 <div className="alert alert-info mt-3">
                     Rivedi i dettagli e conferma la prenotazione.
                 </div>
             )}
          </div>
        );
      default:
        return null;
    }
  };

    // --- Button Disabling Logic ---
  const isNextDisabled = () => {
      if (currentStep === 1 && !selectedCustomer) return true;
      if (currentStep === 2 && !selectedService) return true;
      if (currentStep === 3 && (!selectedDate || selectedTimeSlot === '')) return true;
      return false;
  };

  const isSubmitDisabled = () => {
       return isSubmitting || !selectedCustomer || !selectedService || !selectedDate || selectedTimeSlot === '';
  }


  return (
    <div className="container my-4 reservation-form-container"> {/* Use Bootstrap container */}
      <h1 className="mb-4 text-center">Nuova Prenotazione</h1> {/* Center title */}

      <div className="step-indicator mb-4"> {/* Add margin-bottom */}
        <span className={`step-item ${currentStep >= 1 ? 'active' : ''}`}>1. Cliente</span>
        <span className="separator">›</span>
        <span className={`step-item ${currentStep >= 2 ? 'active' : ''}`}>2. Servizio</span>
        <span className="separator">›</span>
        <span className={`step-item ${currentStep >= 3 ? 'active' : ''}`}>3. Data/Ora</span>
        <span className="separator">›</span>
        <span className={`step-item ${currentStep >= 4 ? 'active' : ''}`}>4. Riepilogo</span>
      </div>

      <div className="step-content p-3 border rounded bg-light mb-4"> {/* Add some padding, border, background */}
        {renderStep()}
      </div>

      <div className="navigation-buttons d-flex justify-content-between"> {/* Use flexbox for buttons */}
        {/* Back Button: Show unless on step 1 or submission was successful */}
        {currentStep > 1 && !submitSuccess && (
          <button className="btn btn-secondary" onClick={prevStep} disabled={isSubmitting}>Indietro</button>
        )}
         {/* Placeholder span to keep 'Avanti'/'Conferma' on the right when Back is hidden */}
         {(currentStep === 1 || submitSuccess) && <span></span>}


        {/* Next Button: Show unless on step 4 */}
        {currentStep < 4 && (
          <button className="btn btn-primary" onClick={nextStep} disabled={isNextDisabled() || isSubmitting}>Avanti</button>
        )}

        {/* Submit Button: Show only on step 4 unless submission was successful */}
        {currentStep === 4 && !submitSuccess && (
          <button className="btn btn-success" onClick={handleSubmit} disabled={isSubmitDisabled()}>
            {isSubmitting ? (
                 <>
                    <span className="spinner-border spinner-border-sm me-2" aria-hidden="true"></span>
                    <span role="status"> Invio...</span>
                 </>
             ) : 'Conferma Prenotazione'}
          </button>
        )}
         {/* New Reservation Button: Show only after successful submission */}
         {currentStep === 4 && submitSuccess && (
             <button className="btn btn-primary" onClick={resetForm}>Nuova Prenotazione</button>
         )}
      </div>
    </div>
  );
};

export default InsertReservation;
