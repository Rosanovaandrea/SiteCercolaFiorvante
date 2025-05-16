import React, { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';

const ServiceList = ({ setServiceToUpdate }) => {
    const [serviceNames, setServiceNames] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const API_BASE_URL = "http://localhost:8080/api/service";

    useEffect(() => {
        const fetchServiceNames = async () => {
            try {
                const response = await axios.get(API_BASE_URL+'/getServices'); // Sostituisci con il tuo endpoint API
                setServiceNames(response.data);
                setLoading(false);
            } catch (error) {
                console.error("Errore nel caricamento dei nomi dei servizi:", error);
                setError("Impossibile caricare la lista dei servizi.");
                setLoading(false);
            }
        };

        fetchServiceNames();
    }, []);

    const handleEditClick = useCallback((event) => {
        if (event.target.classList.contains('btn-outline-primary') && event.target.dataset.serviceName) {
            setServiceToUpdate(event.target.dataset.serviceName);
        }
    }, [setServiceToUpdate]);

    if (loading) {
        return (
            <div className="container mt-4">
                <div className="spinner-border text-primary" role="status">
                    <span className="visually-hidden">Caricamento...</span>
                </div>
                <p className="mt-2">Caricamento della lista dei servizi...</p>
            </div>
        );
    }

    if (error) {
        return <div className="container mt-4 alert alert-danger">{error}</div>;
    }

    return (
        <div className="container mt-4">
            <h2>Lista Servizi</h2>
            <ul className="list-group" onClick={handleEditClick}>
                {serviceNames.map((serviceName) => (
                    <li key={serviceName} className="list-group-item d-flex justify-content-between align-items-center">
                        <span className="fw-bold">{serviceName}</span>
                        <button
                            type="button"
                            className="btn btn-sm btn-outline-primary"
                            data-service-name={serviceName}
                        >
                            <i className="bi bi-pencil-square me-1"></i> Modifica
                        </button>
                    </li>
                ))}
                {serviceNames.length === 0 && <li className="list-group-item">Nessun servizio disponibile.</li>}
            </ul>
        </div>
    );
};

export default ServiceList;