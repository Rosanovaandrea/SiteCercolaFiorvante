import React, { useState } from 'react';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import ServiceList from './ServiceList';
import ServiceEditForm from './ServiceEditForm';

const API_BASE_URL = 'http://localhost:8080/api/service'; // Sostituisci con l'URL del tuo backend Spring

function ServiceManager() {
    const [activeTab, setActiveTab] = useState('insert');
    const [newService, setNewService] = useState({
        serviceName: '',
        price: '',
        description: '',
    });
    const[serviceToUpdate,setUpdateService]=useState("");
    const [images, setImages] = useState([]);

    const handleTabChange = (tab) => {
        setActiveTab(tab);
    };

    const setServiceToUpdate = (serviceToUpdate) => {
        setUpdateService(serviceToUpdate);
        handleTabChange('edit');
    };
    

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setNewService({ ...newService, [name]: value });
    };

    const handleAddImageRow = () => {
        setImages([...images, { file: null, preview: null, name: '' }]);
    };

    const handleImageChange = (index, event) => {
        const newImages = [...images];
        const file = event.target.files[0];

        if (file) {
            newImages[index] = {
                file: file,
                preview: URL.createObjectURL(file),
                name: file.name,
            };
            setImages(newImages);
        } else {
            newImages[index] = { file: null, preview: null, name: '' };
            setImages(newImages);
        }
    };

    const handleRemoveImage = (index) => {
        const newImages = images.filter((_, i) => i !== index);
        setImages(newImages);
    };

    const handleSubmit = async () => {
        const formData = new FormData();
        formData.append('serviceString', JSON.stringify(newService));
        

        images.forEach((image) => {
            if (image.file) {
                formData.append('images', image.file);
            }
        });

        try {
            await axios.post(`${API_BASE_URL}/insertService`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            alert('Servizio aggiunto con successo!');
            setNewService({  serviceName: '', price: '', description: '' });
            setImages([]);
        } catch (error) {
            console.error('Errore durante l\'invio del servizio:', error);
            alert('Errore durante l\'aggiunta del servizio.');
        }
    };

    return (
        <div className="container mt-4">
            <nav className="navbar navbar-expand-lg navbar-light bg-light">
                <div className="container-fluid">
                    <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                        <li className="nav-item">
                            <button
                                className={`nav-link ${activeTab === 'insert' ? 'active' : ''} btn btn-link`}
                                onClick={() => handleTabChange('insert')}
                            >
                                Inserisci Servizio
                            </button>
                        </li>
                        <li className="nav-item">
                            <button
                                className={`nav-link ${activeTab === 'list' ? 'active' : ''} btn btn-link`}
                                onClick={() => handleTabChange('list')}
                            >
                                Elenco Servizi
                            </button>
                        </li>
                    </ul>
                </div>
            </nav>

            <div className="mt-3">
                {activeTab === 'insert' && (
                    <div>
                        <h2>Inserisci Nuovo Servizio</h2>
    
                        <div className="mb-3">
                            <label htmlFor="serviceName" className="form-label">Nome Servizio*</label>
                            <input
                                type="text"
                                className="form-control"
                                id="serviceName"
                                name="serviceName"
                                value={newService.serviceName}
                                onChange={handleInputChange}
                                required
                            />
                        </div>
                        <div className="mb-3">
                            <label htmlFor="price" className="form-label">Prezzo*</label>
                            <input
                                type="number"
                                className="form-control"
                                id="price"
                                name="price"
                                value={newService.price}
                                onChange={handleInputChange}
                                required
                            />
                        </div>
                        <div className="mb-3">
                            <label htmlFor="description" className="form-label">Descrizione*</label>
                            <textarea
                                className="form-control"
                                id="description"
                                name="description"
                                value={newService.description}
                                onChange={handleInputChange}
                                rows="3"
                                required
                            ></textarea>
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Immagini:</label>
                            {images.map((image, index) => (
                                <div key={index} className="row mb-2 align-items-center">
                                    <div className="col-md-4">
                                        <input
                                            type="file"
                                            className="form-control form-control-sm"
                                            onChange={(event) => handleImageChange(index, event)}
                                        />
                                        {image.name && <small className="text-muted">{image.name}</small>}
                                    </div>
                                    <div className="col-md-4">
                                        {image.preview && (
                                            <img
                                                src={image.preview}
                                                alt={`anteprima-${index}`}
                                                className="img-thumbnail"
                                                style={{ maxWidth: '80px', maxHeight: '80px' }}
                                            />
                                        )}
                                    </div>
                                    <div className="col-md-4">
                                        <button
                                            type="button"
                                            className="btn btn-danger btn-sm"
                                            onClick={() => handleRemoveImage(index)}
                                        >
                                            Elimina
                                        </button>
                                    </div>
                                </div>
                            ))}
                            <button type="button" className="btn btn-secondary btn-sm mt-2" onClick={handleAddImageRow}>
                                Aggiungi Immagine
                            </button>
                        </div>
                        <button type="button" className="btn btn-primary" onClick={handleSubmit}>
                            Invia
                        </button>
                    </div>
                )}

                {activeTab === 'list' && <ServiceList setServiceToUpdate={(serviceName) => setServiceToUpdate(serviceName)} />}


                {activeTab === 'edit' && <ServiceEditForm serviceName={serviceToUpdate} />}
            </div>
        </div>
    );
}

export default ServiceManager;