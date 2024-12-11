import { TestBed } from '@angular/core/testing';

import { LocalStorageService } from '../local-storage-service/local-storage.service';

describe('LocalStorageService', () => {
  let service: LocalStorageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LocalStorageService],
    });
    service = TestBed.inject(LocalStorageService);
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('setItem', () => {
    it('should call localStorage.setItem with the correct arguments', () => {
      const key = 'testKey';
      const value = 'testValue';

      // spyOn ensures that the tests mock localStorage behavior and do not interact with the real browser localStorage
      spyOn(localStorage, 'setItem');
      service.setItem(key, value);

      expect(localStorage.setItem).toHaveBeenCalledWith(key, value);
    });
  });

  describe('getItem', () => {
    it('should call localStorage.getItem with the correct key and return the value', () => {
      const key = 'testKey';
      const value = 'testValue';

      spyOn(localStorage, 'getItem').and.returnValue(value);
      const result = service.getItem(key);

      expect(localStorage.getItem).toHaveBeenCalledWith(key);
      expect(result).toBe(value);
    });

    it('should return null if the key does not exist', () => {
      const key = 'nonExistingKey';

      spyOn(localStorage, 'getItem').and.returnValue(null);
      const result = service.getItem(key);

      expect(localStorage.getItem).toHaveBeenCalledWith(key);
      expect(result).toBeNull();
    });
  });

  describe('removeItem', () => {
    it('should call localStorage.removeItem with the correct key', () => {
      const key = 'testKey';

      spyOn(localStorage, 'removeItem');
      service.removeItem(key);

      expect(localStorage.removeItem).toHaveBeenCalledWith(key);
    });
  });

  describe('clear', () => {
    it('should call localStorage.clear', () => {
      spyOn(localStorage, 'clear');
      service.clear();

      expect(localStorage.clear).toHaveBeenCalled();
    });
  });
});
