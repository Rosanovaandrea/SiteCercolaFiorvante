import React, { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';

const ServiceEditForm = ({ serviceName }) => {
    const [originalServiceData, setOriginalServiceData] = useState(null);
    const [editableServiceData, setEditableServiceData] = useState(null);
    const [newImages, setNewImages] = useState([]);
    const [removedImages, setRemovedImages] = useState(new Set());
    const [errors, setErrors] = useState({});
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const API_BASE_URL = 'http://localhost:8080/api/service';
    const IMG_BASE_URL = 'http://localhost:8080';

    useEffect(() => {
        const fetchServiceData = async () => {
            try {
                const response = await axios.get(`${API_BASE_URL}/getSingleService?name=`+serviceName); // Sostituisci con la tua API
                const data = response.data;
                const immutableData = {
                    serviceName: data.serviceName,
                    images: new Set(data.images),
                    price: data.price,
                    description: data.description,
                };
                setOriginalServiceData(immutableData);
                setEditableServiceData({ ...immutableData, images: new Set(immutableData.images) });
                setLoading(false);
            } catch (error) {
                console.error("Errore nel caricamento dei dati del servizio:", error);
                setErrors({ fetch: "Errore nel caricamento dei dati." });
                setLoading(false);
            }
        };

        if (serviceName) {
            fetchServiceData();
        }
    }, [serviceName]);

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setEditableServiceData(prevData => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleAddImage = (event) => {
        const files = Array.from(event.target.files);
        setNewImages(prevImages => [...prevImages, ...files]);
    };

    const handleRemoveExistingImage = (imageName) => {
        setEditableServiceData(prevData => {
            const newImagesSet = new Set(prevData.images);
            newImagesSet.delete(imageName);
            return { ...prevData, images: newImagesSet };
        });
        if (originalServiceData && originalServiceData.images.has(imageName)) {
            setRemovedImages(prevRemovedImages => new Set(prevRemovedImages).add(imageName));
        }
    };

    const handleRemoveNewImage = (index) => {
        setNewImages(prevImages => prevImages.filter((_, i) => i !== index));
    };

    const handleReset = () => {
        if (originalServiceData) {
            setEditableServiceData({ ...originalServiceData, images: new Set(originalServiceData.images) });
            setNewImages([]);
            setRemovedImages(new Set());
            setErrors({});
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        setErrors({});
        setSubmitting(true);

        const newErrors = {};
        if (!editableServiceData?.serviceName) {
            newErrors.serviceName = "Il nome del servizio è obbligatorio.";
        }
        if (editableServiceData?.price === null || editableServiceData?.price === undefined || isNaN(parseFloat(editableServiceData?.price)) || parseFloat(editableServiceData?.price) <= 0) {
            newErrors.price = "Il prezzo deve essere un numero maggiore di zero.";
        }
        if (!editableServiceData?.description) {
            newErrors.description = "La descrizione è obbligatoria.";
        }

        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            setSubmitting(false);
            return;
        }

        const formData = new FormData();

        const service ={
            prevServiceName : originalServiceData.serviceName,
            serviceName : editableServiceData.serviceName,
            price : editableServiceData.price,
            description : editableServiceData.description,
            
        }
        formData.append("serviceString", JSON.stringify(service));

        formData.append("removedImages",Array.from(removedImages));
        
        newImages.forEach(image => {
                    formData.append("images", image);
                });
        

        try {
            const response = await axios.post(`${API_BASE_URL}/updateService`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            console.log("Servizio aggiornato con successo:", response.data);
            // Puoi reindirizzare o mostrare un messaggio di successo qui
        } catch (error) {
            console.error("Errore nell'aggiornamento del servizio:", error);
            setErrors({ submit: "Errore nell'aggiornamento del servizio." });
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) {
        return <div className="container">Caricamento dati...</div>;
    }

    if (!editableServiceData) {
        return <div className="container">Impossibile caricare il form.</div>;
    }

    return (
        <div className="container mt-4">
            <h2>Modifica Servizio</h2>
            {errors.fetch && <div className="alert alert-danger">{errors.fetch}</div>}
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label htmlFor="serviceName" className="form-label">Nome Servizio</label>
                    <input
                        type="text"
                        className={`form-control ${errors.serviceName ? 'is-invalid' : ''}`}
                        id="serviceName"
                        name="serviceName"
                        value={editableServiceData.serviceName}
                        onChange={handleInputChange}
                    />
                    {errors.serviceName && <div className="invalid-feedback">{errors.serviceName}</div>}
                </div>

                <div className="mb-3">
                    <label htmlFor="price" className="form-label">Prezzo</label>
                    <input
                        type="number"
                        className={`form-control ${errors.price ? 'is-invalid' : ''}`}
                        id="price"
                        name="price"
                        value={editableServiceData.price}
                        onChange={handleInputChange}
                    />
                    {errors.price && <div className="invalid-feedback">{errors.price}</div>}
                </div>

                <div className="mb-3">
                    <label htmlFor="description" className="form-label">Descrizione</label>
                    <textarea
                        className={`form-control ${errors.description ? 'is-invalid' : ''}`}
                        id="description"
                        name="description"
                        value={editableServiceData.description}
                        onChange={handleInputChange}
                    />
                    {errors.description && <div className="invalid-feedback">{errors.description}</div>}
                </div>

                <div className="mb-3">
                    <label className="form-label">Immagini Esistenti</label>
                    <div className="row">
                        {Array.from(editableServiceData.images).map((imageName, index) => (
                            <div key={index} className="col-md-3 mb-2">
                                <div className="card">
                                    <div className="card-body">
                                        <h6 className="card-title">{imageName}</h6>
                                        {/* Inserisci qui l'anteprima dell'immagine se hai l'URL */}
                                        { <img src={`${IMG_BASE_URL}/images/${imageName}`} alt={imageName} className="img-fluid mb-2" /> }
                                        <button
                                            type="button"
                                            className="btn btn-danger btn-sm"
                                            onClick={() => handleRemoveExistingImage(imageName)}
                                        >
                                            Rimuovi
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))}
                        {Array.from(editableServiceData.images).length === 0 && <p>Nessuna immagine esistente.</p>}
                    </div>
                </div>

                <div className="mb-3">
                    <label htmlFor="newImages" className="form-label">Aggiungi Nuove Immagini</label>
                    <input
                        type="file"
                        className="form-control"
                        id="newImages"
                        multiple
                        onChange={handleAddImage}
                    />
                    {newImages.length > 0 && (
                        <div className="mt-2">
                            <p>Anteprima Nuove Immagini:</p>
                            <div className="row">
                                {newImages.map((file, index) => (
                                    <div key={index} className="col-md-3 mb-2">
                                        <div className="card">
                                            <div className="card-body">
                                                <h6 className="card-title">{file.name}</h6>
                                                <img
                                                    src={URL.createObjectURL(file)}
                                                    alt={file.name}
                                                    className="img-fluid mb-2"
                                                    style={{ maxHeight: '100px' }}
                                                />
                                                <button
                                                    type="button"
                                                    className="btn btn-danger btn-sm"
                                                    onClick={() => handleRemoveNewImage(index)}
                                                >
                                                    Rimuovi
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}
                </div>

                {errors.submit && <div className="alert alert-danger">{errors.submit}</div>}

                <button type="submit" className="btn btn-primary" disabled={submitting}>
                    {submitting ? 'Aggiornamento in corso...' : 'Aggiorna Servizio'}
                </button>
                <button type="button" className="btn btn-secondary ms-2" onClick={handleReset} disabled={submitting}>
                    Reset
                </button>
            </form>
        </div>
    );
};

export default ServiceEditForm;